package com.thoughtworks.wechat_core.util.precondition;

import org.junit.Test;

import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ArgumentPreconditionTest {

    private final String ERROR_MESSAGE = "message";

    @Test(expected = PreconditionException.class)
    public void testCheckNotBlank_Null() throws Exception {
        checkNotBlank(null);
    }

    @Test(expected = PreconditionException.class)
    public void testCheckNotBlank_Empty() throws Exception {
        checkNotBlank("");
    }

    @Test(expected = PreconditionException.class)
    public void testCheckNotBlank_Space() throws Exception {
        checkNotBlank("  ");
    }

    @Test
    public void testCheckNotBlank_Success() throws Exception {
        checkNotBlank("data");
    }

    @Test()
    public void testCheckNotBlank_Null_WithMessage() throws Exception {
        try {
            checkNotBlank(null, ERROR_MESSAGE);
        } catch (PreconditionException ex) {
            assertThat(ex.getMessage(), equalTo(ERROR_MESSAGE));
        }
    }

    @Test
    public void testCheckNotBlank_Empty_WithMessage() throws Exception {
        try {
            checkNotBlank("", ERROR_MESSAGE);
        } catch (PreconditionException ex) {
            assertThat(ex.getMessage(), equalTo(ERROR_MESSAGE));
        }
    }

    @Test
    public void testCheckNotBlank_Space_WithMessage() throws Exception {
        try {
            checkNotBlank("  ", ERROR_MESSAGE);
        } catch (PreconditionException ex) {
            assertThat(ex.getMessage(), equalTo(ERROR_MESSAGE));
        }
    }

    @Test
    public void testCheckNotBlank_Success_WithMessage() throws Exception {
        checkNotBlank("data", ERROR_MESSAGE);
    }

}