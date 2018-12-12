package net.runelite.client.ui;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.util.SwingUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.runelite.client.ui.ClientUI.ICON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
    author:     Marek Urban
    file ver.:  0.0.1
    rev. year:  2018
*/

@RunWith(MockitoJUnitRunner.class)
public class NotifierTest {

    @Mock
    private ClientUI clientUI;
    @Mock
    private Client client;

    private Notifier notifier;
    private TrayIcon trayIcon;
    private RuneLiteProperties runeLiteProperties;

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    @Before
    public void setUp() {
        RuneLiteConfig runeLiteConfig = new RuneLiteConfig() {
        };
        runeLiteProperties = new RuneLiteProperties();
        ContainableFrame frame = mock(ContainableFrame.class);
        trayIcon = SwingUtil.createTrayIcon(ICON, runeLiteProperties.getTitle(), frame);
        when(clientUI.getTrayIcon()).thenReturn(trayIcon);
        notifier = new Notifier(clientUI, client, runeLiteConfig, runeLiteProperties, scheduledExecutorService);
    }

    @Test
    public void testSkipNotification() {
        ContainableFrame frame = mock(ContainableFrame.class);
        RuneLiteProperties runeLiteProperties = mock(RuneLiteProperties.class);

        trayIcon = SwingUtil.createTrayIcon(ICON, runeLiteProperties.getTitle(), frame);
        RuneLiteConfig runeLiteConfig = mock(RuneLiteConfig.class);
        Notifier notifierMockConf = new Notifier(clientUI, client, runeLiteConfig, runeLiteProperties, scheduledExecutorService);
        notifierMockConf.notify("Notification test message!");
        trayIcon.getToolTip();
        assertNull(trayIcon.getToolTip());
    }


    @Test
    public void runNotificationTests() {
        for (int i = 0; i < TrayIcon.MessageType.values().length; i++) {
            testNotifyWithIcon(TrayIcon.MessageType.values()[i]);
        }
    }

    @Test
    public void testNotifyWithoutIcon() {
        boolean success = false;

        try {
            notifier.notify("Notification test message!");
            TimeUnit.SECONDS.sleep(2);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        trayIcon.getToolTip();
        assertTrue(success);
        assertEquals(trayIcon.getToolTip(), runeLiteProperties.getTitle());
    }

    public void testNotifyWithIcon(TrayIcon.MessageType type) {
        boolean success = false;
        try {
            notifier.notify("Notification test message!", type);
            TimeUnit.SECONDS.sleep(2);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(success);
        assertEquals(trayIcon.getToolTip(), runeLiteProperties.getTitle());
    }

}
