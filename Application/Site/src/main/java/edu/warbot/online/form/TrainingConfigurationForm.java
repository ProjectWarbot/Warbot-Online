package edu.warbot.online.form;

import edu.warbot.online.models.TrainingConfiguration;
import edu.warbot.online.models.enums.LevelTrainingConfigurationEnum;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by jimmy on 14/04/15.
 */


public class TrainingConfigurationForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @NotBlank(message = TrainingConfigurationForm.NOT_BLANK_MESSAGE)
    public String zoneName;

    @NotBlank(message = TrainingConfigurationForm.NOT_BLANK_MESSAGE)
    public String level;


    public String description;

    public TrainingConfiguration createTestZone() {
        TrainingConfiguration tz = new TrainingConfiguration(zoneName, LevelTrainingConfigurationEnum.valueOf(level), description);
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
