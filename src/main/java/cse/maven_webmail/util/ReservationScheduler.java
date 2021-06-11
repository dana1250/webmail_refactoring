/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.util;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author kim-yeonghwa
 */
public class ReservationScheduler extends HttpServlet{

    
    private static final long serialVersionUID = -4013616887475315494L;
    static private SchedulerFactory schedulerFactory;
    static private Scheduler scheduler;

    public ReservationScheduler() {
        try {
            schedulerFactory  = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        try {
            JobDetail job1 = new JobDetail("job1", Scheduler.DEFAULT_GROUP, ReservationExecutor.class);
            CronTrigger trigger1  = new CronTrigger("job1", Scheduler.DEFAULT_GROUP, "0 0/1 * * * ?");
            scheduler.deleteJob(job1.getKey().getName(),job1.getKey().getGroup());
            scheduler.scheduleJob(job1, trigger1);
            scheduler.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException ex) {
            Logger.getLogger(ReservationScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
