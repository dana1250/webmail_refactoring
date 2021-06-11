/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 책임: enctype이 multipart/form-data인 HTML 폼에 있는 각 필드와 파일 정보 추출
 *
 * @author jongmin
 */
public class FormParser {

    private HttpServletRequest request;
    private String toAddress = null;
    private String ccAddress = null;
    private String subject = null;
    private String body = null;
    private String fileName = null;
    private String uploadTargetDir = "C:/temp/upload";
    // 202105 KYH - @ 예약 여부 및 예약 시간 추가
    private boolean isReservation = false;
    private Date reservationDate = null;
    private String aliasFileName = null;
//    private String year = null;
//    private String momth = null;
//    private String day = null;
//    private String hour = null;
//    private String minute = null;
    

    public FormParser(HttpServletRequest request) {
        this.request = request;
        if (System.getProperty("os.name").equals("Linux")) {
            uploadTargetDir = request.getServletContext().getRealPath("/WEB-INF")
                    + File.separator + "upload";
            File f = new File(uploadTargetDir);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        // 202105 KYH @ - MAC OS X 첨부파일 대응
        else if (System.getProperty("os.name").contains("Mac")) {
            uploadTargetDir = request.getServletContext().getRealPath("/WEB-INF")
                    + File.separator + "upload";
            File f = new File(uploadTargetDir);
            if (!f.exists()) {
                f.mkdir();
            }
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAliasFileName() {
        return aliasFileName;
    }

    public void setAliasFileName(String aliasFileName) {
        this.aliasFileName = aliasFileName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUploadTargetDir() {
        return uploadTargetDir;
    }

    public void setUploadTargetDir(String uploadTargetDir) {
        this.uploadTargetDir = uploadTargetDir;
    }

    public boolean getIsReservation() {
        return isReservation;
    }

    public void setIsReservation(boolean isReservation) {
        this.isReservation = isReservation;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

//    public String getYear() {
//        return year;
//    }
//
//    public void setYear(String year) {
//        this.year = year;
//    }
//
//    public String getMomth() {
//        return momth;
//    }
//
//    public void setMomth(String momth) {
//        this.momth = momth;
//    }
//
//    public String getDay() {
//        return day;
//    }
//
//    public void setDay(String day) {
//        this.day = day;
//    }
//
//    public String getHour() {
//        return hour;
//    }
//
//    public void setHour(String hour) {
//        this.hour = hour;
//    }
//
//    public String getMinute() {
//        return minute;
//    }
//
//    public void setMinute(String minute) {
//        this.minute = minute;
//    }
    
    

    public void parse() {
        try {
            request.setCharacterEncoding("UTF-8");

            // 1. 디스크 기반 파일 항목에 대한 팩토리 생성
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // 2. 팩토리 제한사항 설정
            diskFactory.setSizeThreshold(10 * 1024 * 1024);
            diskFactory.setRepository(new File(this.uploadTargetDir));
            // 3. 파일 업로드 핸들러 생성
            ServletFileUpload upload = new ServletFileUpload(diskFactory);

            // 4. request 객체 파싱
            List fileItems = upload.parseRequest(request);
            Iterator i = fileItems.iterator();
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (fi.isFormField()) {  // 5. 폼 필드 처리
                    String fieldName = fi.getFieldName();
                    String item = fi.getString("UTF-8");

                    if (fieldName.equals("to")) {
                        if (item != null){
                            setToAddress(item);
                        }
                        // 200102 LJM - @ 이후의 서버 주소 제거
                    } else if (fieldName.equals("cc")) {
                        if (item != null){
                           setCcAddress(item); 
                        }
                    } else if (fieldName.equals("subj")) {
                        if (item != null){
                            setSubject(item);
                        }                        
                    } else if (fieldName.equals("body")) {
                        if (item != null){
                            setBody(item);
                        }
                    } else if (fieldName.equals("reservation")) {
                        // 예약메일 설정 여부 확인
                        setIsReservation(true);
                    } else if (fieldName.equals("date")) {
                        if (getIsReservation()) {
                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date = transFormat.parse(item.replace("T", " "));
                            setReservationDate(date);
                        }
                        // 예약메일 시간 저장
                        // 기존 year, month, day,,, 형식의 저장이 아닌
                        // Date 타입의 변수로 저장.
                        // yyyy-MM-ddThh:mm 포맷 적용
//                        System.out.println(item);
//                        if (getIsReservation()) {
//                            setYear(item.substring(0, 4));
//                            setMomth(item.substring(5, 7));
//                            setDay(item.substring(8, 10));
//                            setHour(item.substring(11, 13));
//                            setMinute(item.substring(14, 16));
//                        }
                    }
                } else {  // 6. 첨부 파일 처리
                    if (!(fi.getName() == null || fi.getName().equals(""))) {
                        String fieldName = fi.getFieldName();
                        System.out.println("ATTACHED FILE : " + fieldName + " = " + fi.getName());

                        // 절대 경로 저장
                        setFileName(uploadTargetDir + "/" + fi.getName());
                        setAliasFileName(fi.getName());
                        File fn = new File(fileName);
                        // upload 완료. 추후 메일 전송후 해당 파일을 삭제하도록 해야 함.
                        if (fileName != null) {
                            fi.write(fn);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("FormParser.parse() : exception = " + ex);
        }
    }  // parse()
}