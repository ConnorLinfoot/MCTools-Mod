package com.connorlinfoot.mctools.Hypixel;

public enum GameMode {
    SKYWARS,
    BLITZ_SG,
    CRAZY_WALLS,
    COPS_AND_CRIMS,
    THE_WALLS,
    MEGA_WALLS,
    UHC_CHAMPIONS,
//    BEDWARS,
    UNKNOWN;

    public String getNiceName() {
        switch (this) {
            case SKYWARS:
                return "SkyWars";
            case BLITZ_SG:
                return "Survival Games";
            case CRAZY_WALLS:
                return "Crazy Walls";
            case COPS_AND_CRIMS:
                return "Cops and Crims";
            case THE_WALLS:
                return "The Walls";
            case MEGA_WALLS:
                return "Mega Walls";
            case UHC_CHAMPIONS:
                return "UHC Champions";
//            case BEDWARS:
//                return "BedWars";
            case UNKNOWN:
                return "Unknown";
        }
        return this.toString();
    }

    public String getAPIName() {
        switch (this) {
            case SKYWARS:
                return "SkyWars";
            case BLITZ_SG:
                return "HungerGames";
            case CRAZY_WALLS:
                return "TrueCombat";
            case COPS_AND_CRIMS:
                return "MCGO";
            case THE_WALLS:
                return "Walls";
            case MEGA_WALLS:
                return "Walls3";
            case UHC_CHAMPIONS:
                return "UHC";
            case UNKNOWN:
                return "Unknown";
        }
        return this.toString();
    }

}
