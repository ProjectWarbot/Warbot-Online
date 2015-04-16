package edu.warbot.form;

import edu.warbot.models.TestZone;
import edu.warbot.models.enums.LevelTestZoneEnum;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by jimmy on 14/04/15.
 */


public class TestZoneForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @NotBlank(message = TestZoneForm.NOT_BLANK_MESSAGE)
    public String zoneName;

    @NotBlank(message = TestZoneForm.NOT_BLANK_MESSAGE)
    public String level;

    @NotBlank(message = TestZoneForm.NOT_BLANK_MESSAGE)
    public String description;

    public TestZone createTestZone() {
        TestZone tz = new TestZone(zoneName, LevelTestZoneEnum.valueOf(level), description);
        return tz;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
