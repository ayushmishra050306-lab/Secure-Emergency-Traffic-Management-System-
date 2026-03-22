package ejb.message;

import ejb.remote.TrafficDataManager;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

import java.util.LinkedHashMap;
import java.util.Map;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "myQueue")
        }
)
public class TrafficDataReceiverBean implements MessageListener {
    @EJB
    TrafficDataManager trafficDataManager;

    @Override
    public void onMessage(Message message) {

        Map trafficData = null;
        try {
            trafficData = message.getBody(Map.class);
            trafficDataManager.saveTrafficData((LinkedHashMap) trafficData);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
