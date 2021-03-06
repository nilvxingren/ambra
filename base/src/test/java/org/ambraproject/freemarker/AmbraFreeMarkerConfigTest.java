package org.ambraproject.freemarker;

import org.ambraproject.action.BaseTest;
import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

/**
 * @author Joe Osowski
 */
public class AmbraFreeMarkerConfigTest extends BaseTest {
  @Autowired()
  protected @Qualifier("ambraConfiguration2") Configuration ambraConfiguration;

  @Test
  public void testConfig() throws Exception {
    AmbraFreemarkerConfig config = new AmbraFreemarkerConfig(ambraConfiguration);

    String name = config.getDisplayNameByEissn("1234");
    String name1 = config.getDisplayNameByEissn("5678");

    assertEquals(name, "test journal");
    assertEquals(name1, "test journal 1");

    String hashTag = config.getHashTag("journal");
    String hashTag1 = config.getHashTag("journal1");

    assertEquals(hashTag, "#JOURNAL");
    assertEquals(hashTag1, "#JOURNALONE");
  }
}
