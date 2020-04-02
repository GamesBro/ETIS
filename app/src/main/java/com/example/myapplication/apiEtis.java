package com.example.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class apiEtis{
   private String session_id = null;

   public apiEtis(){}
   public apiEtis(String session_id){
      this.session_id = session_id;
   }

   public String getTimeTable(boolean cons, int week) {
      try {
         System.out.println(session_id);
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.timetable?p_cons="+(cons?'y':'n')+"&p_week="+ week);
         HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      return null;
   }

   public String getRatingCurrent(){
      try {
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.signs?p_mode=current");
         HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      return null;
   }

   public String getRatingSession(){
      try {
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.signs?p_mode=session");
         HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      return null;
   }

   public String getRatingDiplom(){
      try {
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.signs?p_mode=diplom");
         HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

   public String getCurriculumShort(){
      try {
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.teach_plan?p_mode=short");
         HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

   public String getAbsence(int p_term) throws IOException {
      URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.absence?p_term="+p_term);
      HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
      connection.setRequestProperty("Cookie", session_id);

      if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
         return readStream(connection.getInputStream());
      }
      return null;
   }

   public String getOrders(){
      try {
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.orders");
         HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

   public String getAnnounce(){
      try {
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.announce");
         HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

    public String getElectricRes(){
        try {
            URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.electr");
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestProperty("Cookie", session_id);

            if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                return readStream(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

   public String getEducationalStandard(){
      try {
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.electr");
         HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

   public String getTeachers(){
      try {
         URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.teachers");
         HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
         connection.setRequestProperty("Cookie", session_id);

         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            return readStream(connection.getInputStream());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

   public String auth(String login, String password) throws IOException {
        /*
            https://student.psu.ru/pls/stu_cus_et/stu.login

            Host: student.psu.ru
            User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0
            Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*\/*;q=0.8
            Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3
            Accept-Encoding: gzip, deflate, br
            Content-Type: application/x-www-form-urlencoded
            Content-Length: 72
            Origin: https://student.psu.ru
            DNT: 1
            Connection: keep-alive
            Referer: https://student.psu.ru/pls/stu_cus_et/stu.teach_plan
            Upgrade-Insecure-Requests: 1

            p_redirect	stu.teach_plan
            p_username	%D1%FB%F7%E5%E2
            p_password	pass
        */

      URL url = new URL("https://student.psu.ru/pls/stu_cus_et/stu.login");
      HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

      // Send post request
      String params = "p_redirect=stu.teach_plan&p_username="+URLEncoder.encode(login, "windows-1251")+"&p_password="+password;
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
      wr.writeBytes(params);
      wr.flush();
      wr.close();

      int responseCode = connection.getResponseCode();
      if(responseCode == HttpsURLConnection.HTTP_OK){
         String server_response = readStream(connection.getInputStream());
         /**/System.out.println("Data page : " + server_response);
      }

      //List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
      String SetCookie = connection.getHeaderField("Set-Cookie");
      if(SetCookie != null)
         this.session_id = SetCookie.split(";")[0];

      return this.session_id;
   }

   private static String readStream(InputStream in) {
      BufferedReader reader = null;
      StringBuffer response = new StringBuffer();
      try {
         reader = new BufferedReader(new InputStreamReader(in, "windows-1251"));
         String line;
         while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return response.toString();
   }
}