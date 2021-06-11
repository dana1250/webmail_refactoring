/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.util;

import cse.maven_webmail.model.SmtpAgent;
import cse.maven_webmail.model.loadDBConfig;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author kim-yeonghwa
 */
public class ReservationExecutor implements Job {
    
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException { 
        Connection conn = null;
        PreparedStatement pstmt = null;
        String className = loadDBConfig.getInstance().getDriver();
        String url = loadDBConfig.getInstance().getUrl();
        String User = loadDBConfig.getInstance().getId();
        String Password = loadDBConfig.getInstance().getPw();
        ArrayList<Integer> finish_list = new ArrayList<>();
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(url, User, Password);
            Statement stmt = conn.createStatement();
            
            String sql = "SELECT * FROM reservation_mail";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date dateTime = form.parse(rs.getString("reservation_date"));
                if(dateTime.compareTo(new Date()) > 0){
                    continue;
                }
                //SmtpAgent 객체에 메일 관련 정보 설정
                SmtpAgent agent = new SmtpAgent(rs.getString("host"), rs.getString("user_id"));
                agent.setTo(rs.getString("toaddr"));
                agent.setCc(rs.getString("ccaddr"));
                agent.setSubj(rs.getString("subject"));
                agent.setBody(rs.getString("body"));
                String fileName = rs.getString("fileName");
                System.out.println("WriteMailHandler.sendMessage() : fileName = " + fileName);
                if (fileName != null) {
                    InputStream fileInputStream = rs.getBinaryStream("file");
                    agent.setFile1(fileName);
                    agent.setIsReservationMail(true);
                    agent.setFileStream(fileInputStream);
                }
                //메일 전송 권한 위임
                if (agent.sendMessage()) {
                    finish_list.add(Integer.parseInt(rs.getString("id")));
                }
            }

            // 예약전송이 완료된 메일 삭제
            for(int i:finish_list){
                String deleteSql = "DELETE FROM reservation_mail WHERE id=?";
                pstmt = conn.prepareStatement(deleteSql);
                pstmt.setString(1, Integer.toString(i));
                pstmt.executeUpdate();
            }
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
