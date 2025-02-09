package com.orange.lo.sample.mqtt2dis.dis;

import com.huaweicloud.dis.iface.data.request.PutRecordsRequest;
import com.huaweicloud.dis.iface.data.response.PutRecordsResult;
import com.huaweicloud.dis.iface.data.response.PutRecordsResultEntry;
import com.huaweicloud.dis.DIS;
import com.orange.lo.sample.mqtt2dis.utils.Counters;

import io.micrometer.core.instrument.Counter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DISMessageSenderTest {

    @Mock
    private DISProperties disProperties;
    @Mock
    private PutRecordsResult putRecordsResult;
    @Mock
    private DIS disClient;
    @Mock
    private Counters counters;
    @Mock
    private Counter evtAttempt;
    @Mock
    private Counter evtFailed;
    @Mock
    private Counter evtSent;
    
    private DISMessageSender disMessageSender;

    @BeforeEach
    void setUp() {
    	when(counters.evtAttempt()).thenReturn(evtAttempt);
    	when(counters.evtFailed()).thenReturn(evtFailed);
    	when(counters.evtSent()).thenReturn(evtSent);
        when(disClient.putRecords(any(PutRecordsRequest.class))).thenReturn(putRecordsResult);
        when(putRecordsResult.getFailedRecordCount()).thenReturn(new AtomicInteger(0));
        when(putRecordsResult.getRecords()).thenReturn(new ArrayList<>());
        this.disMessageSender = new DISMessageSender(disProperties, disClient, counters);
    }

    @Test
    void shouldCallDISWhenMessagesAreSent() {
        disMessageSender.send(Arrays.asList("message-one", "message-two"));

        verify(disClient, times(1)).putRecords(any(PutRecordsRequest.class));
    }
}