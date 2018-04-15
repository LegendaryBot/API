package com.greatmancode.legendarybotapi.discorduser;

import com.greatmancode.legendarybotapi.utils.GenericIdJsonTable;
import com.greatmancode.legendarybotapi.utils.WoWCharacter;

import java.util.List;

public interface DiscordUser extends GenericIdJsonTable {

    List<WoWCharacter> getCharacters();
    void updateCharacters(List<WoWCharacter> characters);
}
