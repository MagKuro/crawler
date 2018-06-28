package browser;

import opennlp.tools.stemmer.PorterStemmer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.panforge.robotstxt.RobotsTxt;

import java.net.URL;
import java.util.*;

public class Crawler {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    private List<Page> pages;
    private int parentId;
    private int childId;
    private static String HOME;

    public Crawler() {
        pages = new ArrayList<Page>();
    }

    public List<Page> getPages() {
        return pages;
    }

    public void addNewPageToPages(String url) {
        Page page = new Page(url, createLocalTerms(url));
        pages.add(page);
    }

    public void callGetPageLinks(String home) {
        HOME = home;
        for (int i = 0; i < 1 && i < pages.size(); i++) {
            //System.out.println("SPRAWDZAM DLA: "+ pages.get(i).getUrl());
            parentId = i;
            getPageLinks(pages.get(i));
            //System.out.println("KONIEC CYKLU " + i);
        }
    }

    public boolean isPagesContainsPage(String url) {
        return pages.stream().anyMatch(a -> a.getUrl().equals(url));
    }

    //znajdz indeks elementu o zadanym url
    public int foundChildId(String url) {
        Page pageFound = null;
        Optional optional = pages.stream().filter(a -> a.getUrl().equals(url)).findFirst();
        if (optional.isPresent()) {
            pageFound = (Page) optional.get();
        }
        return pages.indexOf(pageFound);
    }

    public void getPageLinks(Page page) {

        boolean hasAccess = true;
        try {
            InputStream robotsTxtStream = new URL(page.getUrl()).openStream();
            RobotsTxt robotsTxt = RobotsTxt.read(robotsTxtStream);
            hasAccess = robotsTxt.query(null, "/robots.txt");

        } catch (IOException e) {
            //e.printStackTrace();
        }
        try {
            if (hasAccess) {
                Connection connection = Jsoup.connect(page.getUrl()).userAgent(USER_AGENT);
                Document doc = connection.get();
               /* String htmlString = Jsoup.parse(doc.toString()).select("body").text();
                System.out.println(htmlString);*/

                if (connection.response().contentType().contains("text/html")) {
                    List<Element> linksOnPage = doc.select("a[href]");
                    //oczyszczenie linksOnPage z duplikatow
                    Set<String> urls = new HashSet<>();
                    for (Element linkOnPage : linksOnPage) {
                        urls.add(linkOnPage.attr("href"));
                    }

                    for (String linkOnPage : urls) {
                        if (linkOnPage.startsWith(HOME)) {  //sprawdzam domene
                            if (!isPagesContainsPage(linkOnPage)) {     //lista pages nie zawiera strony
                                addNewPageToPages(linkOnPage);   //dodaj strone do listy
                                childId = pages.size() - 1;
                            } else {
                                if (foundChildId(linkOnPage) != -1) {
                                    childId = foundChildId(linkOnPage);
                                }
                            }

                            //uzupelnij listy rodziców i dzieci
                            pages.get(childId).addParent(pages.get(parentId).getUrl());
                            pages.get(parentId).addChildren(pages.get(childId).getUrl());
                        }
                    }
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public Map<String, Integer> createLocalTerms(String url) {
        PorterStemmer porterStemmer = new PorterStemmer();
        Connection connect = Jsoup.connect(url).userAgent(USER_AGENT);
        Document document = null;
        try {
            document = connect.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String htmlString = Jsoup.parse(document.toString()).select("body").text();
        List<String> words = cleanLocalTermsFromStopWord(new ArrayList<String>(Arrays.asList(htmlString.split("\\W+"))));  //rozczłonkowanie dokumentu na pojedyncze słowa

        Map<String, Integer> localTerms = new HashMap<>();
        for (String word1 : words) {
            String word = porterStemmer.stem(word1);
            if (!localTerms.containsKey(word)) {
                localTerms.put(word, 1);
            } else {
                int numberOfOccurrences = localTerms.get(word) + 1;
                localTerms.put(word, numberOfOccurrences);
            }
        }
        return localTerms;
    }

    private List<String> cleanLocalTermsFromStopWord(List<String> words) {
        List<String> stopWords = new ArrayList<>();
        try {
            Scanner reader = new Scanner(new File("stopwords.txt"));
            while (reader.hasNextLine()) {
                stopWords.add(reader.nextLine());
            }
            words.removeAll(stopWords);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return words;
    }
}
