import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	public static DB db=new DB();
	
	public static void main(String args[]) throws IOException, SQLException
	{
		db.runSql2("TRUNCATE Record;");
		processPage("https://dir.indiamart.com/search.mp?ss=import+steel");
	}
	public static void processPage(String URL) throws IOException, SQLException
	{
		String sql = "select * from Record where URL = '"+URL+"'";
		ResultSet rs = db.runSql(sql);
		if(rs.next()){
 
		}
       
		else{
    	   sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);;
			stmt.setString(1, URL);
			stmt.execute();

    	   Document doc = Jsoup.connect("https://dir.indiamart.com/search.mp?ss=import+steel").get();
    	   if(doc.text().contains("Import Steel")){
				System.out.println(URL);
			}
    	   Elements questions = doc.select("a[href]");
			for(Element link: questions){
				if(link.attr("href").contains("indiamart.com"))
					processPage(link.attr("abs:href"));
			}
       }
	}

}
