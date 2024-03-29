import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import com.sensonet.SensonetApplication;
import com.sensonet.emq.EmqClient;
import com.sensonet.mapper.entity.QuotaEntity;
import com.sensonet.service.QuotaService;
import com.sensonet.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootTest(classes = SensonetApplication.class)
@RunWith(SpringRunner.class)
public class TestEmq {


    @Autowired
    private EmqClient emqClient;

    @Autowired
    private QuotaService quotaService;

    @Test
    public void testSend(){
        // Connect to the MQTT server
        emqClient.connect();
        // Publish a message to the MQTT server
        emqClient.publish("test_topic","test_content");

    }

    @Test
    public void testSimulateMsgInflux(){
        Random random = new Random();

        System.out.println("Simulate 10 device msg at " + LocalDateTime.now());

        List<QuotaEntity> quotaList = quotaService.list();  // Get all quota definitions
        // Simulate 10 devices
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = Maps.newHashMap(); // Simulate payload from device
            HashSet<String> topics = new HashSet<>();

            // Generate random string device ID from 00000 to 99999
            String deviceId = String.format("%05d", random.nextInt(100000));

            // Get all quota definitions
            for (QuotaEntity quotaEntity : quotaList) {
                map.put(quotaEntity.getSnKey(), deviceId); // json "sn" field with value
                // Generate a random value according to the quota type
                switch (quotaEntity.getName()) {
                    case "温度":
                    case "电量": {
                        int quotaValue = random.nextInt(100);
                        map.put(quotaEntity.getValueKey(), quotaValue);
                        break;
                    }
                    case "湿度": {
                        int quotaValue = random.nextInt(80) + 20;
                        map.put(quotaEntity.getValueKey(), quotaValue);
                        break;
                    }
                    case "压强": {
                        int quotaValue = random.nextInt(600) + 101300;
                        map.put(quotaEntity.getValueKey(), quotaValue);
                        break;
                    }
                    case "电源": {
                        int choice = random.nextInt(2);
                        String quotaValue = choice == 0 ? "ON" : "OFF";
                        map.put(quotaEntity.getValueKey(), quotaValue);
                        break;
                    }
                    case "G值": {
                        double quotaValue = random.nextDouble() * 5.0;
                        DecimalFormat df = new DecimalFormat("#.##");
                        map.put(quotaEntity.getValueKey(), df.format(quotaValue));
                        break;
                    }
                }
                topics.add(quotaEntity.getSubject());
            }

            // Turn the map into a json string
            try {
                String json = JsonUtil.serialize(map);
                String infoMsg = "Publish message to " + topics + ": " + json;
                System.out.println(infoMsg);
//                for (String topic: topics) {
//                    emqClient.publish(topic, json); // Publish the message to the MQTT server
//                    Thread.sleep(50);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
