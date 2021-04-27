import java.util.regex.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import com.icia.zboard3.util.*;

public class Test {
	public static void main(String[] args) {
		// ck 이미지 두 장을 포함한 글을 작성
		// c:/upload/temp -> aaa.jpg, bbb.jpg
		// 사진 두장을 포함한 글의 내용은 아래와 같다
		String content = "<p><p><img alt=\"\" src=\"http://localhost:8081/temp/9eac806c-bfbf-4847-bac0-549e1bdab954.jpg\" style=\"height:185px; width:273px\" /></p></p>";
		content += "<div><img src=\"http://localhost:8081/temp/bbb.jpg\"></div>";

		// 1. content에서 aaa.jpg, bbb.jpg라는 이름을 꺼내서 c:/upload/temp에서 c:/upload/image에서 이동
		// html에서 img 태그를 찾아서 각 img 태그의 src속성을 잘라내야 한다 ->
		// http://localhost:8081/temp/aaa.jpg

		// html 문서의 body영역을 읽어와라
		Document document = Jsoup.parseBodyFragment(content);
		Elements elements = document.getElementsByTag("img");
		if (elements.isEmpty() == false) {
			for (Element element : elements) {
				String src = element.attr("src");
				System.out.println(src.substring(src.lastIndexOf("/") + 1));
			}
		}

		// 2. content에서 /temp를 /image로 바꾼다
		System.out.println(content.replaceAll("src=\"http://localhost:8081/temp/", "src=\"http://localhost:8081/image/"));

		String patt = ".*localhost:8081/temp/.*";
		System.out.println(Pattern.matches(patt, "http://localhost:8081/temp/ifweoifj.jpg"));
		System.out.println("http://localhost:8081/temp/ifweoifj.jpg".replaceAll("localhost:8081/temp/", "localhost:8081/image/"));
	}
}
