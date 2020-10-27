package com.insightsystems.dal.cisco;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.api.dal.ping.Pingable;
import com.avispl.symphony.dal.communicator.HttpCommunicator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CP8831 extends HttpCommunicator implements Monitorable, Pingable {
    protected void authenticate() {}

    @Override
    public List<Statistics> getMultipleStatistics() throws Exception {
        final ExtendedStatistics extStats = new ExtendedStatistics();
        final Map<String,String> stats = new HashMap<>();

        final String res = doGet("/");

        stats.put("MacAddress",regexFind(res,"MAC Address.+?<B>([^<]+)<\\/B>"));
        stats.put("Hostname",regexFind(res,"Host Name.+?<B>([^<]+)<\\/B>"));
        stats.put("PhoneDn",regexFind(res,"Phone DN.+?<B>([^<]+)<\\/B>"));
        stats.put("Version",regexFind(res,"Version.+?<B>([^<]+)<\\/B>"));
        stats.put("HardwareRevision",regexFind(res,"Hardware Revision.+?<B>([^<]+)<\\/B>"));
        stats.put("SerialNumber",regexFind(res,"Serial Number.+?<B>([^<]+)<\\/B>"));
        stats.put("ModelNumber",regexFind(res,"Model Number.+?<B>([^<]+)<\\/B>"));
        stats.put("MessageWaiting",regexFind(res,"Message Waiting.+?<B>([^<]+)<\\/B>"));
        stats.put("TimeZone",regexFind(res,"Time Zone.+?<B>([^<]+)<\\/B>"));
        stats.put("SystemFreeMemory",regexFind(res,"System Free Memory.+?<B>([^<]+)<\\/B>"));
        stats.put("JavaHeapFreeMemory",regexFind(res,"Java Heap Free Memory.+?<B>([^<]+)<\\/B>"));
        stats.put("JavaPoolFreeMemory",regexFind(res,"Java Pool Free Memory.+?<B>([^<]+)<\\/B>"));


        stats.put("TimeDate",regexFind(res,"Time.+?<B>([^<]+)<\\/B>") + " " + regexFind(res,"Date.+?<B>([^<]+)<\\/B>"));

        extStats.setStatistics(stats);
        return Collections.singletonList(extStats);
    }
    private String regexFind(String sourceString,String regex){
        final Matcher matcher = Pattern.compile(regex).matcher(sourceString);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static void main(String[] args) throws Exception {
        CP8831 dev = new CP8831();
        dev.setPort(5500);
        dev.setHost("127.0.0.1");
        dev.setProtocol("http");
        dev.init();
        ((ExtendedStatistics) dev.getMultipleStatistics().get(0)).getStatistics().forEach((k,v) -> System.out.println(k +  " : " + v));
    }
}
