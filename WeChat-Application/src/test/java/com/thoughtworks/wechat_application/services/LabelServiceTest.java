package com.thoughtworks.wechat_application.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.configs.CacheConfiguration;
import com.thoughtworks.wechat_application.jdbi.core.Label;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.core.TextMessage;
import com.thoughtworks.wechat_application.jdbi.LabelDAO;
import com.thoughtworks.wechat_application.utils.CacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LabelServiceTest {
    @Mock
    private LabelDAO labelDAO;
    @Mock
    private CacheConfiguration configuration;


    @Before
    public void setUp() throws Exception {
        when(configuration.getLabelCacheSeconds()).thenReturn(10);
    }

    @Test
    public void testInject() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(LabelDAO.class).toInstance(labelDAO);
            binder.bind(CacheConfiguration.class).toInstance(configuration);
        });

        final LabelService labelService = injector.getInstance(LabelService.class);
        assertThat(labelService, notNullValue());
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(LabelDAO.class).toInstance(labelDAO);
            binder.bind(CacheConfiguration.class).toInstance(configuration);
        });

        final LabelService labelService = injector.getInstance(LabelService.class);
        final LabelService anotherLabelService = injector.getInstance(LabelService.class);
        assertThat(labelService, equalTo(anotherLabelService));
    }

    @Test
    public void testCreateLabel_NoCache_NotExist() throws Exception {
        final Label label1 = createLabel1();
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(), Arrays.asList(label1));
        when(labelDAO.createLabel(eq(label1.getTitle()), any(Timestamp.class))).thenReturn(label1.getId());

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final Optional<Label> label = service.createLabel(label1.getTitle());

        verify(labelDAO, times(2)).getAllLabel();
        verify(labelDAO).createLabel(eq(label1.getTitle()), any(Timestamp.class));

        assertThat(label.isPresent(), equalTo(true));
        assertThat(label.get().getId(), equalTo(1L));
        assertThat(label.get().getTitle(), equalTo(label1.getTitle()));
    }

    @Test
    public void testCreateLabel_WithCache_NotExist() throws Exception {
        final Label label2 = createLabel2();
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel1()), Arrays.asList(createLabel1(), label2));
        when(labelDAO.createLabel(eq(label2.getTitle()), any(Timestamp.class))).thenReturn(label2.getId());

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.getAllLabels();
        final Optional<Label> label = service.createLabel(label2.getTitle());

        verify(labelDAO, times(2)).getAllLabel();
        verify(labelDAO).createLabel(eq(label2.getTitle()), any(Timestamp.class));

        assertThat(label.isPresent(), equalTo(true));
        assertThat(label.get().getId(), equalTo(2L));
        assertThat(label.get().getTitle(), equalTo(label2.getTitle()));
    }

    @Test
    public void testCreateLabel_NoCache_Exist() throws Exception {
        final Label label1 = createLabel1();
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(label1, createLabel2()));
        when(labelDAO.createLabel(eq(label1.getTitle()), any(Timestamp.class))).thenReturn(label1.getId());

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final Optional<Label> label = service.createLabel(label1.getTitle());

        verify(labelDAO).getAllLabel();
        verify(labelDAO, never()).createLabel(eq(label1.getTitle()), any(Timestamp.class));

        assertThat(label.isPresent(), equalTo(false));
    }

    @Test
    public void testCreateLabel_WithCache_Exist() throws Exception {
        final Label label1 = createLabel1();
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(label1, createLabel2()));
        when(labelDAO.createLabel(eq(label1.getTitle()), any(Timestamp.class))).thenReturn(label1.getId());

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.getAllLabels();
        final Optional<Label> label = service.createLabel(label1.getTitle());

        verify(labelDAO).getAllLabel();
        verify(labelDAO, never()).createLabel(eq(label1.getTitle()), any(Timestamp.class));

        assertThat(label.isPresent(), equalTo(false));
    }

    @Test
    public void testGetAllLabel_NoData() throws Exception {
        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final List<Label> labels = service.getAllLabels();

        verify(labelDAO).getAllLabel();
        assertThat(labels, notNullValue());
        assertThat(labels.isEmpty(), equalTo(true));
    }

    @Test
    public void testGetAllLabel_HaveData() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final List<Label> labels = service.getAllLabels();

        verify(labelDAO).getAllLabel();
        assertThat(labels, notNullValue());
        assertThat(labels.isEmpty(), equalTo(false));
        assertThat(labels.size(), equalTo(2));
    }

    @Test
    public void testGetAllLabel_WithCache() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.getAllLabels();
        final List<Label> labels = service.getAllLabels();

        verify(labelDAO).getAllLabel();
        assertThat(labels, notNullValue());
        assertThat(labels.isEmpty(), equalTo(false));
        assertThat(labels.size(), equalTo(2));
    }

    @Test
    public void testGetAllLabel_CacheExpired() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.getAllLabels();
        Thread.sleep(configuration.getLabelCacheSeconds() * 1000L);
        final List<Label> labels = service.getAllLabels();

        verify(labelDAO, times(2)).getAllLabel();
        assertThat(labels, notNullValue());
        assertThat(labels.isEmpty(), equalTo(false));
        assertThat(labels.size(), equalTo(2));
    }

    @Test
    public void testGet_NoCache_NotExist() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final Optional<Label> label = service.get("Label1");

        verify(labelDAO).getAllLabel();
        assertThat(label.isPresent(), equalTo(false));
    }

    @Test
    public void testGet_NoCache_Exist() throws Exception {
        final Label label1 = createLabel1();
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(label1, createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final Optional<Label> label = service.get("Label1");

        verify(labelDAO).getAllLabel();
        assertThat(label.isPresent(), equalTo(true));
        assertThat(label.get().getId(), equalTo(1L));
        assertThat(label.get().getTitle(), equalTo(label1.getTitle()));
    }

    @Test
    public void testGet_WithCache_NotExist() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.getAllLabels();
        final Optional<Label> label = service.get("Label1");

        verify(labelDAO).getAllLabel();
        assertThat(label.isPresent(), equalTo(false));
    }

    @Test
    public void testGet_WithCache_Exist() throws Exception {
        final Label label1 = createLabel1();
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(label1, createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.getAllLabels();
        final Optional<Label> label = service.get("Label1");

        verify(labelDAO).getAllLabel();
        assertThat(label.isPresent(), equalTo(true));
        assertThat(label.get().getId(), equalTo(1L));
        assertThat(label.get().getTitle(), equalTo(label1.getTitle()));
    }

    @Test
    public void testDeleteLabel_NoCache_NotExist() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.deleteLabel("Label1");

        verify(labelDAO).getAllLabel();
        verify(labelDAO, never()).deleteLabel(1L);
    }

    @Test
    public void testDeleteLabel_NoCache_Exist() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.deleteLabel("Label1");

        verify(labelDAO).getAllLabel();
        verify(labelDAO).deleteLabel(1L);
    }

    @Test
    public void testDeleteLabel_WithCache_NotExist() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.getAllLabels();
        service.deleteLabel("Label1");

        verify(labelDAO).getAllLabel();
        verify(labelDAO, never()).deleteLabel(1L);
    }

    @Test
    public void testDeleteLabel_WithCache_Exist() throws Exception {
        when(labelDAO.getAllLabel()).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        service.getAllLabels();
        service.deleteLabel("Label1");

        verify(labelDAO).getAllLabel();
        verify(labelDAO).deleteLabel(1L);
    }


    @Test
    public void testGetMemberLabels() throws Exception {
        final Label label1 = createLabel1();
        when(labelDAO.getMemberLabel(eq(1L))).thenReturn(label1);

        final Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final Optional<Label> memberLabels = service.getMemberLabels(member);

        verify(labelDAO).getMemberLabel(eq(1L));
        assertThat(memberLabels.isPresent(), equalTo(true));
        assertThat(memberLabels.get().getId(), equalTo(1L));
        assertThat(memberLabels.get().getTitle(), equalTo(label1.getTitle()));
    }

    @Test
    public void testGetMemberLabels_NoLabel() throws Exception {
        final Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final Optional<Label> memberLabels = service.getMemberLabels(member);

        verify(labelDAO).getMemberLabel(eq(1L));
        assertThat(memberLabels.isPresent(), equalTo(false));
    }

    @Test
    public void testGetTextMessageLabels() throws Exception {
        when(labelDAO.getTextMessageLabels(eq(1L))).thenReturn(Arrays.asList(createLabel1(), createLabel2()));

        final TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getId()).thenReturn(1L);

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final List<Label> labels = service.getTextMessageLabels(textMessage);

        verify(labelDAO).getTextMessageLabels(eq(1L));
        assertThat(labels, notNullValue());
        assertThat(labels.size(), equalTo(2));
    }

    @Test
    public void testGetTextMessageLabels_NoLabel() throws Exception {
        when(labelDAO.getTextMessageLabels(eq(1L))).thenReturn(new ArrayList<>());

        final TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getId()).thenReturn(1L);

        final LabelService service = new LabelService(labelDAO, configuration, new CacheManager());
        final List<Label> labels = service.getTextMessageLabels(textMessage);

        verify(labelDAO).getTextMessageLabels(eq(1L));
        assertThat(labels, notNullValue());
        assertThat(labels.isEmpty(), equalTo(true));
    }

    private Label createLabel1() {
        return new Label(1, "Label1");
    }

    private Label createLabel2() {
        return new Label(2, "Label2");
    }
}