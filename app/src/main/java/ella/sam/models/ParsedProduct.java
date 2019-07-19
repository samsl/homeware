package ella.sam.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParsedProduct {

    private String imgUrl;
    private String brand;
    private String category;
    private String name;
    private String capacity;
    private String press;
    private Date publishDate;
    private String isbn;

    @JsonIgnore
    private String url;
    @JsonIgnore
    private Document document;

    public ParsedProduct(String url) {
        this.url = url;
        this.parse();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    private String getValid(String select1, String attr1, String select2, String attr2) {
        String result = document.select(select1).attr(attr1);
        if (StringUtils.isEmpty(result)) {
            return document.select(select2).attr(attr2);
        }
        return result;
    }

    private void parse() {
        try {
            String pattern = ".*/(.*)html.*";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                url = "https://item.jd.com/" + m.group(1) + "html";
            } else {
                throw new RuntimeException("Parsed url failed");
            }
            this.document = Jsoup.connect(url).get();
            this.imgUrl = "https:" + getValid("img#spec-img", "data-origin", "div#spec-n1>img", "src");
            this.name = document.select("ul.parameter2>li:contains(商品名称)").attr("title");
            this.category = getValid("ul.parameter2>li:contains(分类)", "title", "ul.parameter2>li:contains(类别)", "title");
            this.brand = getValid("ul#parameter-brand>li", "title", "ul.parameter2>li:contains(品牌)", "title");
            this.press = document.select("ul#parameter2>li:contains(出版社)").attr("title");
            if (!StringUtils.isEmpty(this.press)) {
                this.isbn = document.select("ul#parameter2>li:contains(ISBN)").attr("title");
                this.category = "书";
                this.name = document.select("div#name>div.sku-name").text();
                this.capacity = document.select("ul#parameter2>li:contains(页数)").attr("title");
                this.publishDate = new SimpleDateFormat("yyyy-MM-dd").parse(document.select("ul#parameter2>li:contains(出版时间)").attr("title"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Parsed url failed", e);
        } catch (ParseException e) {
            throw new RuntimeException("Parsed date failed", e);
        }
    }
}
