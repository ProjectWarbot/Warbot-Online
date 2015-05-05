package edu.warbot.online;

/**
 * Created by beugnon on 05/04/15.
 */
public class WebGameSettings
{

    private Long idTeam1;

    private Long idTeam2;

    public WebGameSettings()
    {
        this.idTeam1= new Long(0);
        this.idTeam2= new Long(0);
    }

    public WebGameSettings(Long idTeam1, Long idTeam2) {
        this.idTeam1 = idTeam1;
        this.idTeam2 = idTeam2;
    }

    public Long getIdTeam1() {
        return idTeam1;
    }

    public void setIdTeam1(Long idTeam1) {
        this.idTeam1 = idTeam1;
    }

    public Long getIdTeam2() {
        return idTeam2;
    }

    public void setIdTeam2(Long idTeam2) {
        this.idTeam2 = idTeam2;
    }
}
