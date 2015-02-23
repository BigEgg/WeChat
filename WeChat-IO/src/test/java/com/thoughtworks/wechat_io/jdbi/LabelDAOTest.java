package com.thoughtworks.wechat_io.jdbi;

import com.thoughtworks.wechat_io.core.Label;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class LabelDAOTest extends AbstractDAOTest {
    private LabelDAO labelDAO;
    private MemberDAO memberDAO;
    private TextMessageDAO textMessageDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        memberDAO = getDAO(MemberDAO.class);
        labelDAO = getDAO(LabelDAO.class);
        textMessageDAO = getDAO(TextMessageDAO.class);
    }

    @Test
    public void testCreateLabel() throws Exception {
        long id = labelDAO.createLabel("Label1", getHappenedTime());
        assertThat(id, equalTo(1L));
        id = labelDAO.createLabel("Label2", getHappenedTime());
        assertThat(id, equalTo(2L));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateLabel_WithSameName() throws Exception {
        labelDAO.createLabel("Label", getHappenedTime());
        labelDAO.createLabel("Label", getHappenedTime());
    }

    @Test
    public void testGetAllLabel_NoLabel() throws Exception {
        List<Label> labels = labelDAO.getAllLabel();
        assertThat(labels, notNullValue());
        assertThat(labels.isEmpty(), equalTo(true));
    }

    @Test
    public void testGetAllLabel() throws Exception {
        labelDAO.createLabel("Label1", getHappenedTime());
        labelDAO.createLabel("Label2", getHappenedTime());

        List<Label> labels = labelDAO.getAllLabel();
        assertThat(labels, notNullValue());
        assertThat(labels.isEmpty(), equalTo(false));
        assertThat(labels.size(), equalTo(2));
    }

    @Test
    public void testDeleteLabel() throws Exception {
        labelDAO.createLabel("Label1", getHappenedTime());
        labelDAO.createLabel("Label2", getHappenedTime());

        List<Label> labels = labelDAO.getAllLabel();
        assertThat(labels.size(), equalTo(2));

        labelDAO.deleteLabel(labels.get(0).getId());
        labels = labelDAO.getAllLabel();
        assertThat(labels.size(), equalTo(1));
        assertThat(labels.get(0).getId(), equalTo(2L));
        assertThat(labels.get(0).getName(), equalTo("Label2"));
    }

    @Test
    public void testGetMemberLabels() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, labelId);

        Label label = labelDAO.getMemberLabels(memberId);
        assertThat(label, notNullValue());
        assertThat(label.getId(), equalTo(labelId));
        assertThat(label.getName(), equalTo("Label"));
    }

    @Test
    public void testGetMemberLabels_NoLink() throws Exception {
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());

        Label label = labelDAO.getMemberLabels(memberId);
        assertThat(label, nullValue());
    }

    @Test
    public void testGetMemberLabels_NoMember() throws Exception {
        Label label = labelDAO.getMemberLabels(0);
        assertThat(label, nullValue());
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testDeleteLabel_HaveMember() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        memberDAO.linkMemberWithLabel(memberId, labelId);

        labelDAO.deleteLabel(labelId);
    }

    @Test
    public void testGetTextMessageLabels() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long messageId = textMessageDAO.createTextMessage("title", "content", getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(messageId, labelId);

        List<Label> labels = labelDAO.getTextMessageLables(messageId);
        assertThat(labels, notNullValue());
        assertThat(labels.size(), equalTo(1));
        assertThat(labels.get(0).getId(), equalTo(labelId));
        assertThat(labels.get(0).getName(), equalTo("Label"));
    }

    @Test
    public void testGetTextMessageLabels_NoLink() throws Exception {
        final long messageId = textMessageDAO.createTextMessage("title", "content", getHappenedTime());

        List<Label> labels = labelDAO.getTextMessageLables(messageId);
        assertThat(labels, notNullValue());
        assertThat(labels.size(), equalTo(0));
    }

    @Test
    public void testGetTextMessageLabels_NoMember() throws Exception {
        List<Label> labels = labelDAO.getTextMessageLables(0);
        assertThat(labels, notNullValue());
        assertThat(labels.size(), equalTo(0));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testDeleteLabel_HaveTextMessage() throws Exception {
        final long labelId = labelDAO.createLabel("Label", getHappenedTime());
        final long messageId = textMessageDAO.createTextMessage("title", "content", getHappenedTime());
        textMessageDAO.linkTextMessageWithLabel(messageId, labelId);

        labelDAO.deleteLabel(labelId);
    }
}