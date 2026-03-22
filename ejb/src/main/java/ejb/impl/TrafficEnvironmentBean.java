package ejb.impl;

import ejb.remote.TrafficEnvironment;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Singleton
@Lock(LockType.READ)
public class TrafficEnvironmentBean implements TrafficEnvironment {
    private static final double STANDARD_DEVIATION = 1.0; // 1 hour
    private static final Random random = new Random();

    private int meanMorningPeak;
    private int morningPeakDuration;
    private int meanAfternoonPeak;
    private int afternoonPeakDuration;
    private int meanEveningPeak;
    private int eveningPeakDuration;
    private int meanOtherPeak;
    private int otherPeakDuration;
    private double currentTrafficDensity;
    private long lastUpdateTime;

    public int randomValueGenerator() {
        return 30 + random.nextInt(30);
    }

    @PostConstruct
    public synchronized void updatePeakHours() {
        meanMorningPeak = random.nextInt(2) + 8; // between 8 and 9
        morningPeakDuration = randomValueGenerator();
        meanAfternoonPeak = random.nextInt(2) + 14; // between 14 and 15
        afternoonPeakDuration = randomValueGenerator();
        meanEveningPeak = random.nextInt(2) + 17; // between 17 and 18
        eveningPeakDuration = randomValueGenerator();

        System.out.println(meanMorningPeak + " " + morningPeakDuration + "| " + meanAfternoonPeak + " " + afternoonPeakDuration + "| " + meanEveningPeak + " " + eveningPeakDuration);
    }

    public synchronized void updateTrafficDensity(Date dateTimestamp) {

        LocalDateTime localDateTime = LocalDateTime.ofInstant(dateTimestamp.toInstant(), ZoneId.systemDefault());
        LocalTime now = localDateTime.toLocalTime();

        // Update peak hour if
        if (now.getHour() == 0 && now.getMinute() == 0) {
            updatePeakHours();
        }

        double meanPeakHour;
        double peakDuration = 0;

//        if(y<2){
//            System.out.println(LocalTime.of(meanMorningPeak, 0).minusMinutes((long) (morningPeakDuration / 2)));
//            System.out.println(LocalTime.of(meanMorningPeak, 0).plusMinutes((long) (morningPeakDuration / 2)));
//            y++;
//        }

        if (isBetween(now, LocalTime.of(meanMorningPeak, 0).minusMinutes((long) (morningPeakDuration / 2)), LocalTime.of(meanMorningPeak, 0).plusMinutes(((long) (morningPeakDuration / 2))))) {
            meanPeakHour = meanMorningPeak;
            peakDuration = Double.parseDouble(String.format("%.2f", morningPeakDuration / 100.0));
        } else if (isBetween(now, LocalTime.of(meanAfternoonPeak, 0).minusMinutes((long) (afternoonPeakDuration / 2)), LocalTime.of(meanAfternoonPeak + 1, 0).plusMinutes((long) (afternoonPeakDuration / 2)))) {
            meanPeakHour = meanAfternoonPeak;
            peakDuration = Double.parseDouble(String.format("%.2f", afternoonPeakDuration / 100.0));
        } else if (isBetween(now, LocalTime.of(meanEveningPeak, 0).minusMinutes((long) (eveningPeakDuration / 2)), LocalTime.of(meanEveningPeak + 1, 0).plusMinutes((long) (eveningPeakDuration / 2)))) {
            meanPeakHour = meanEveningPeak;
            peakDuration = Double.parseDouble(String.format("%.2f", eveningPeakDuration / 100.0));
        } else if (isBetween(now, LocalTime.of(meanOtherPeak, 0).minusMinutes((long) (otherPeakDuration / 2)), LocalTime.of(meanOtherPeak + 1, 0).plusMinutes((long) (otherPeakDuration / 2)))) {
            meanPeakHour = meanOtherPeak;
            peakDuration = Double.parseDouble(String.format("%.2f", otherPeakDuration / 100.0));
        } else {
            meanPeakHour = 0.0; // Off peak
        }


        double timeSincePeakStart = Math.abs(meanPeakHour - now.getHour());
        if (timeSincePeakStart == 0.0) {
            timeSincePeakStart = Math.abs(now.getHour() - meanPeakHour);
        }
        double progressThroughPeak = timeSincePeakStart / peakDuration;
        double peakTrafficDensity = 1;
        if (meanPeakHour != 0.0) {
            peakTrafficDensity = Math.exp(-Math.pow(progressThroughPeak - 0.5, 2) / (2 * STANDARD_DEVIATION * STANDARD_DEVIATION));
        }

        System.out.println(now + " | " + timeSincePeakStart + " | " + progressThroughPeak + " | " + peakTrafficDensity + " | " + peakTrafficDensity * 50);

        currentTrafficDensity = peakTrafficDensity;
        lastUpdateTime = dateTimestamp.getTime();
    }

    public synchronized double getTrafficDensity(Date dateTimestamp) {
        if (dateTimestamp.getTime() - lastUpdateTime > 2 * 60 * 60 * 1000) {
            boolean isSelect = random.nextBoolean();
            if (isSelect) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("H");
                String format = dateFormat.format(dateTimestamp);
                meanOtherPeak = Integer.parseInt(format) + 1;
                otherPeakDuration = randomValueGenerator();
                System.out.println("isSelect " + isSelect + " " + meanOtherPeak + " " + otherPeakDuration);
            }

        }
        updateTrafficDensity(dateTimestamp);
        return currentTrafficDensity;
    }

    private boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
        return start.isBefore(time) && end.isAfter(time);
    }
}
