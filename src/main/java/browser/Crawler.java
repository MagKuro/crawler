package browser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    public Crawler(){
        pages = new ArrayList<Page>();
    }

    public List<Page> getPages() {
        return pages;
    }

    public void addNewPageToList(String url){
        pages.add(new Page(url));
    }

    public void callGetPageLinks(){
        for(int i=0; i<20 && i<pages.size(); i++) {
            //System.out.println("SPRAWDZAM DLA: "+ pages.get(i).getUrl());
            parentId = i;
            getPageLinks(pages.get(i));
            //System.out.println("KONIEC CYKLU " + i);
        }
    }

    public boolean ifListContainPageWithUrl(String url){
        return pages.stream().anyMatch(a -> a.getUrl().equals(url));
    }

    //znajdz indeks elementu o zadanym url
    public int foundChildId(String url){
        Page pageFound = null;
        Optional optional = pages.stream().filter(a -> a.getUrl().equals(url)).findFirst();
        if(optional.isPresent()){
            pageFound = (Page) optional.get();
        }
        return pages.indexOf(pageFound);
    }

    public void getPageLinks(Page page){

        boolean hasAccess = true;
        try {
            InputStream robotsTxtStream = new URL(page.getUrl()).openStream();
            RobotsTxt robotsTxt = RobotsTxt.read(robotsTxtStream);
            hasAccess = robotsTxt.query(null,"/robots.txt");

        } catch (IOException e) {
            //e.printStackTrace();
        }
        try {
            //if(hasAccess) {
//                Document doc = Jsoup.connect(page.getUrl()).get();
//                List<Element> linksOnPage = doc.select("a[href]");
            Connection connection = Jsoup.connect(page.getUrl()).userAgent(USER_AGENT);
            Document doc = connection.get();
            String htmlString = Jsoup.parse(doc.toString()).select("body").text();
            System.out.println(htmlString);

            if (connection.response().contentType().contains("text/html")){
                List<Element> linksOnPage = doc.select("a[href]");
            //oczyszczenie linksOnPage z duplikatow
            Set<String> urls = new HashSet<>();
            for (Element linkOnPage : linksOnPage) {
                urls.add(linkOnPage.attr("href"));
            }

            for (String linkOnPage : urls) {
               // if (linkOnPage.startsWith("https://www.ohio")) {  //sprawdzam domene
                    //System.out.println("linkOnPage: "+linkOnPage);
                    if (!ifListContainPageWithUrl(linkOnPage)) {     //lista nie zawiera strony
                        //System.out.println("nowy link na stronie: "+linkOnPage);
                        addNewPageToList(linkOnPage);   //dodaj strone do listy
                        childId = pages.size() - 1;
                    } else {
                        if (foundChildId(linkOnPage) != -1) {
                            childId = foundChildId(linkOnPage);
                            //System.out.println("childId wynosi:"+ childId);
                        }
                    }
                    //uzupelnij listy rodziców i dzieci
                    pages.get(childId).addParent(pages.get(parentId).getUrl());
                    pages.get(parentId).addChildren(pages.get(childId).getUrl());
              // }
            }
        }
           // }
        } catch (IOException e) {
                    //e.printStackTrace();
            }
        }
}
