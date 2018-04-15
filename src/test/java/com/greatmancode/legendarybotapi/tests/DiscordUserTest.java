package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.discorduser.DiscordUser;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserImpl;
import com.greatmancode.legendarybotapi.oauth.OAuthHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class DiscordUserTest {

    @Test
    public void testDiscordUser() {
        String resultingJson = "{\n" +
                "    \"characters\": [\n" +
                "        {\n" +
                "            \"name\": \"Huulurrah\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 11,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 0,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"arthas/149/191471509-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Valvan\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 10,\n" +
                "            \"race\": 26,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 100,\n" +
                "            \"achievementPoints\": 11450,\n" +
                "            \"thumbnail\": \"arthas/154/156614810-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Mistweaver\",\n" +
                "                \"role\": \"HEALING\",\n" +
                "                \"backgroundImage\": \"bg-monk-mistweaver\",\n" +
                "                \"icon\": \"spell_monk_mistweaver_spec\",\n" +
                "                \"description\": \"A healer who masters the mysterious art of manipulating life energies, aided by the wisdom of the Jade Serpent and Pandaren medicinal techniques.\",\n" +
                "                \"order\": 1\n" +
                "            },\n" +
                "            \"guild\": \"Legendary\",\n" +
                "            \"guildRealm\": \"Arthas\",\n" +
                "            \"lastModified\": 1483203734000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Kogobank\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 1,\n" +
                "            \"race\": 2,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 1,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"arthas/76/157999948-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Illunia\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 7,\n" +
                "            \"race\": 11,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 27,\n" +
                "            \"achievementPoints\": 11215,\n" +
                "            \"thumbnail\": \"mugthol/2/101722882-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Elemental\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-shaman-elemental\",\n" +
                "                \"icon\": \"spell_nature_lightning\",\n" +
                "                \"description\": \"A spellcaster who harnesses the destructive forces of nature and the elements.\",\n" +
                "                \"order\": 0\n" +
                "            },\n" +
                "            \"lastModified\": 1457895853000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Azshanne\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 5,\n" +
                "            \"race\": 9,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 76,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"mugthol/234/102045930-avatar.jpg\",\n" +
                "            \"lastModified\": 1408136394000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Barkbek\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 4,\n" +
                "            \"race\": 8,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 91,\n" +
                "            \"achievementPoints\": 9905,\n" +
                "            \"thumbnail\": \"arthas/175/159198127-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Subtlety\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-rogue-subtlety\",\n" +
                "                \"icon\": \"ability_stealth\",\n" +
                "                \"description\": \"A dark stalker who leaps from the shadows to ambush his unsuspecting prey.\",\n" +
                "                \"order\": 2\n" +
                "            },\n" +
                "            \"lastModified\": 1468769860000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Anhain\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 1,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 34,\n" +
                "            \"achievementPoints\": 15590,\n" +
                "            \"thumbnail\": \"arthas/196/188212676-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Protection\",\n" +
                "                \"role\": \"TANK\",\n" +
                "                \"backgroundImage\": \"bg-warrior-protection\",\n" +
                "                \"icon\": \"ability_warrior_defensivestance\",\n" +
                "                \"description\": \"A stalwart protector who uses a shield to safeguard herself and her allies.\",\n" +
                "                \"order\": 2\n" +
                "            },\n" +
                "            \"lastModified\": 1507601956000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Muiri\",\n" +
                "            \"realm\": \"Shadow Council\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 3,\n" +
                "            \"race\": 4,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 15,\n" +
                "            \"achievementPoints\": 11235,\n" +
                "            \"thumbnail\": \"shadow-council/66/45262914-avatar.jpg\",\n" +
                "            \"lastModified\": 1458524921000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Abranu\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 3,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 22,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"mugthol/151/101893271-avatar.jpg\",\n" +
                "            \"guild\": \"ME ME ME ME BANK\",\n" +
                "            \"guildRealm\": \"Mug'thol\",\n" +
                "            \"lastModified\": 1424800169000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Dondek\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 6,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 100,\n" +
                "            \"achievementPoints\": 15590,\n" +
                "            \"thumbnail\": \"arthas/55/156058935-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Frost\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-deathknight-frost\",\n" +
                "                \"icon\": \"spell_deathknight_frostpresence\",\n" +
                "                \"description\": \"An icy harbinger of doom, channeling runic power and delivering vicious weapon strikes.\",\n" +
                "                \"order\": 1\n" +
                "            },\n" +
                "            \"lastModified\": 1506908610000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Eamandrick\",\n" +
                "            \"realm\": \"Tichondrius\",\n" +
                "            \"battlegroup\": \"Bloodlust\",\n" +
                "            \"class\": 2,\n" +
                "            \"race\": 1,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 85,\n" +
                "            \"achievementPoints\": 9915,\n" +
                "            \"thumbnail\": \"tichondrius/28/167375644-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Holy\",\n" +
                "                \"role\": \"HEALING\",\n" +
                "                \"backgroundImage\": \"bg-paladin-holy\",\n" +
                "                \"icon\": \"spell_holy_holybolt\",\n" +
                "                \"description\": \"Invokes the power of the Light to protect and to heal.\",\n" +
                "                \"order\": 0\n" +
                "            },\n" +
                "            \"guild\": \"BEEP BEEP IM A JEEP LOL\",\n" +
                "            \"guildRealm\": \"Tichondrius\",\n" +
                "            \"lastModified\": 1437783953000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Tong\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 3,\n" +
                "            \"race\": 26,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 110,\n" +
                "            \"achievementPoints\": 15530,\n" +
                "            \"thumbnail\": \"arthas/0/162810880-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Beast Mastery\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-hunter-beastmaster\",\n" +
                "                \"icon\": \"ability_hunter_bestialdiscipline\",\n" +
                "                \"description\": \"A master of the wild who can tame a wide variety of beasts to assist him in combat.\",\n" +
                "                \"order\": 0\n" +
                "            },\n" +
                "            \"guild\": \"Legendary\",\n" +
                "            \"guildRealm\": \"Arthas\",\n" +
                "            \"lastModified\": 1518138772000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Xiaye\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 10,\n" +
                "            \"race\": 24,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 1,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"mugthol/0/102091520-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Argoramul\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 1,\n" +
                "            \"race\": 2,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 1,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"arthas/245/186511349-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Kugruon\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 11,\n" +
                "            \"race\": 28,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 110,\n" +
                "            \"achievementPoints\": 15760,\n" +
                "            \"thumbnail\": \"arthas/169/156057769-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Balance\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-druid-balance\",\n" +
                "                \"icon\": \"spell_nature_starfall\",\n" +
                "                \"description\": \"Can take on the form of a powerful Moonkin, balancing the power of Arcane and Nature magic to destroy enemies at a distance.\",\n" +
                "                \"order\": 0\n" +
                "            },\n" +
                "            \"guild\": \"Legendary\",\n" +
                "            \"guildRealm\": \"Arthas\",\n" +
                "            \"lastModified\": 1523736555000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Barimdor\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 9,\n" +
                "            \"race\": 8,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 86,\n" +
                "            \"achievementPoints\": 15590,\n" +
                "            \"thumbnail\": \"arthas/144/158223248-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Affliction\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-warlock-affliction\",\n" +
                "                \"icon\": \"spell_shadow_deathcoil\",\n" +
                "                \"description\": \"A master of shadow magic who specializes in drains and damage-over-time spells.\",\n" +
                "                \"order\": 0\n" +
                "            },\n" +
                "            \"lastModified\": 1506908133000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Thunian\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 3,\n" +
                "            \"race\": 4,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 25,\n" +
                "            \"achievementPoints\": 11205,\n" +
                "            \"thumbnail\": \"mugthol/34/101735458-avatar.jpg\",\n" +
                "            \"lastModified\": 1457895883000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Juulia\",\n" +
                "            \"realm\": \"The Venture Co\",\n" +
                "            \"battlegroup\": \"Shadowburn\",\n" +
                "            \"class\": 6,\n" +
                "            \"race\": 11,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 55,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"the-venture-co/47/127769135-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Mathil\",\n" +
                "            \"realm\": \"Tichondrius\",\n" +
                "            \"battlegroup\": \"Bloodlust\",\n" +
                "            \"class\": 2,\n" +
                "            \"race\": 1,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 100,\n" +
                "            \"achievementPoints\": 10100,\n" +
                "            \"thumbnail\": \"tichondrius/36/167375652-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Holy\",\n" +
                "                \"role\": \"HEALING\",\n" +
                "                \"backgroundImage\": \"bg-paladin-holy\",\n" +
                "                \"icon\": \"spell_holy_holybolt\",\n" +
                "                \"description\": \"Invokes the power of the Light to protect and to heal.\",\n" +
                "                \"order\": 0\n" +
                "            },\n" +
                "            \"guild\": \"BEEP BEEP IM A JEEP LOL\",\n" +
                "            \"guildRealm\": \"Tichondrius\",\n" +
                "            \"lastModified\": 1468956471000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Hannu\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 7,\n" +
                "            \"race\": 28,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 68,\n" +
                "            \"achievementPoints\": 15760,\n" +
                "            \"thumbnail\": \"arthas/251/190996219-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Restoration\",\n" +
                "                \"role\": \"HEALING\",\n" +
                "                \"backgroundImage\": \"bg-shaman-restoration\",\n" +
                "                \"icon\": \"spell_nature_magicimmunity\",\n" +
                "                \"description\": \"A healer who calls upon ancestral spirits and the cleansing power of water to mend allies' wounds.\",\n" +
                "                \"order\": 2\n" +
                "            },\n" +
                "            \"guild\": \"Legendary\",\n" +
                "            \"guildRealm\": \"Arthas\",\n" +
                "            \"lastModified\": 1522016603000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Kogorof\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 5,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 110,\n" +
                "            \"achievementPoints\": 15760,\n" +
                "            \"thumbnail\": \"arthas/79/155697487-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Shadow\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-priest-shadow\",\n" +
                "                \"icon\": \"spell_shadow_shadowwordpain\",\n" +
                "                \"description\": \"Uses sinister Shadow magic, especially damage-over-time spells, to eradicate enemies.\",\n" +
                "                \"order\": 2\n" +
                "            },\n" +
                "            \"guild\": \"Legendary\",\n" +
                "            \"guildRealm\": \"Arthas\",\n" +
                "            \"lastModified\": 1523733352000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Urtzerzzul\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 7,\n" +
                "            \"race\": 8,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 85,\n" +
                "            \"achievementPoints\": 7285,\n" +
                "            \"thumbnail\": \"arthas/118/159198070-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Restoration\",\n" +
                "                \"role\": \"HEALING\",\n" +
                "                \"backgroundImage\": \"bg-shaman-restoration\",\n" +
                "                \"icon\": \"spell_nature_magicimmunity\",\n" +
                "                \"description\": \"A healer who calls upon ancestral spirits and the cleansing power of water to mend allies' wounds.\",\n" +
                "                \"order\": 2\n" +
                "            },\n" +
                "            \"lastModified\": 1518364942000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Kraznix\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 7,\n" +
                "            \"race\": 9,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 9,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"mugthol/112/101957744-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Garganof\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 7,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 100,\n" +
                "            \"achievementPoints\": 12795,\n" +
                "            \"thumbnail\": \"arthas/54/158129462-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Enhancement\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-shaman-enhancement\",\n" +
                "                \"icon\": \"spell_shaman_improvedstormstrike\",\n" +
                "                \"description\": \"A totemic warrior who strikes foes with weapons imbued with elemental power.\",\n" +
                "                \"order\": 1\n" +
                "            },\n" +
                "            \"lastModified\": 1509313000000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Luutas\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 2,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 90,\n" +
                "            \"achievementPoints\": 10315,\n" +
                "            \"thumbnail\": \"arthas/215/160833751-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Retribution\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-paladin-retribution\",\n" +
                "                \"icon\": \"spell_holy_auraoflight\",\n" +
                "                \"description\": \"A righteous crusader who judges and punishes opponents with weapons and Holy magic.\",\n" +
                "                \"order\": 2\n" +
                "            },\n" +
                "            \"lastModified\": 1468769984000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Zeshamy\",\n" +
                "            \"realm\": \"Shadow Council\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 7,\n" +
                "            \"race\": 11,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 9,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"shadow-council/196/43160260-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Lookatmynose\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 3,\n" +
                "            \"race\": 9,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 43,\n" +
                "            \"achievementPoints\": 9855,\n" +
                "            \"thumbnail\": \"mugthol/61/101975357-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Marksmanship\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-hunter-marksman\",\n" +
                "                \"icon\": \"ability_hunter_focusedaim\",\n" +
                "                \"description\": \"A master archer or sharpshooter who excels in bringing down enemies from afar.\",\n" +
                "                \"order\": 1\n" +
                "            },\n" +
                "            \"lastModified\": 1424488568000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Drazbrezlok\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 8,\n" +
                "            \"race\": 8,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 85,\n" +
                "            \"achievementPoints\": 11320,\n" +
                "            \"thumbnail\": \"arthas/30/170067742-avatar.jpg\",\n" +
                "            \"lastModified\": 1452916506000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Nebank\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 7,\n" +
                "            \"race\": 2,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 1,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"mugthol/35/101782819-avatar.jpg\",\n" +
                "            \"guild\": \"ME ME ME ME BANK\",\n" +
                "            \"guildRealm\": \"Mug'thol\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Tanadrandra\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 8,\n" +
                "            \"race\": 10,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 1,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"mugthol/89/125359193-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Valluniina\",\n" +
                "            \"realm\": \"Shadow Council\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 2,\n" +
                "            \"race\": 11,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 4,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"shadow-council/29/42544413-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Belkimi\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 1,\n" +
                "            \"race\": 2,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 1,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"arthas/121/171543417-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Gotnofriend\",\n" +
                "            \"realm\": \"Shadow Council\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 8,\n" +
                "            \"race\": 10,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 6,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"shadow-council/214/44320726-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Nerak\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 6,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 85,\n" +
                "            \"achievementPoints\": 11820,\n" +
                "            \"thumbnail\": \"mugthol/123/101818491-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Frost\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-deathknight-frost\",\n" +
                "                \"icon\": \"spell_deathknight_frostpresence\",\n" +
                "                \"description\": \"An icy harbinger of doom, channeling runic power and delivering vicious weapon strikes.\",\n" +
                "                \"order\": 1\n" +
                "            },\n" +
                "            \"lastModified\": 1513726548000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Andrideana\",\n" +
                "            \"realm\": \"Tichondrius\",\n" +
                "            \"battlegroup\": \"Bloodlust\",\n" +
                "            \"class\": 8,\n" +
                "            \"race\": 1,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 15,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"tichondrius/169/167503785-avatar.jpg\",\n" +
                "            \"guild\": \"BEEP BEEP IM A JEEP LOL\",\n" +
                "            \"guildRealm\": \"Tichondrius\",\n" +
                "            \"lastModified\": 1436580987000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Renalex\",\n" +
                "            \"realm\": \"Shadow Council\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 9,\n" +
                "            \"race\": 1,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 13,\n" +
                "            \"achievementPoints\": 9235,\n" +
                "            \"thumbnail\": \"shadow-council/26/47298330-avatar.jpg\",\n" +
                "            \"lastModified\": 1422035354000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Uldomer\",\n" +
                "            \"realm\": \"Darkspear\",\n" +
                "            \"battlegroup\": \"Cyclone\",\n" +
                "            \"class\": 11,\n" +
                "            \"race\": 22,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 100,\n" +
                "            \"achievementPoints\": 13640,\n" +
                "            \"thumbnail\": \"darkspear/62/82618174-avatar.jpg\",\n" +
                "            \"spec\": {\n" +
                "                \"name\": \"Balance\",\n" +
                "                \"role\": \"DPS\",\n" +
                "                \"backgroundImage\": \"bg-druid-balance\",\n" +
                "                \"icon\": \"spell_nature_starfall\",\n" +
                "                \"description\": \"Can take on the form of a powerful Moonkin, balancing the power of Arcane and Nature magic to destroy enemies at a distance.\",\n" +
                "                \"order\": 0\n" +
                "            },\n" +
                "            \"guild\": \"The Derp Crew\",\n" +
                "            \"guildRealm\": \"Darkspear\",\n" +
                "            \"lastModified\": 1505610645000\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Kugruonn\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 4,\n" +
                "            \"race\": 9,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 1,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"arthas/49/183588913-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Trollpatrol\",\n" +
                "            \"realm\": \"Mug'thol\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 4,\n" +
                "            \"race\": 8,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 11,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"mugthol/181/102060213-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Kerwine\",\n" +
                "            \"realm\": \"Shadow Council\",\n" +
                "            \"battlegroup\": \"Reckoning\",\n" +
                "            \"class\": 1,\n" +
                "            \"race\": 4,\n" +
                "            \"gender\": 1,\n" +
                "            \"level\": 5,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"shadow-council/185/42436025-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        DiscordUser user = DiscordUserHelper.getDiscordUser(1234);
        user.setJson(new JSONObject().toString());
        user = OAuthHelper.handleCharacterUpdate("us", user, resultingJson);
        JSONObject userJSON = new JSONObject(user.getJson());

        assertEquals(40,userJSON.getJSONArray("characters").length());
        user = OAuthHelper.handleCharacterUpdate("us", user, resultingJson);
        userJSON = new JSONObject(user.getJson());
        assertEquals(40,userJSON.getJSONArray("characters").length());


        JSONArray array = DiscordUserHelper.getGuildCharactersForUser(user, "Legendary");
        assertEquals(5, array.length());
        assertEquals(0, DiscordUserHelper.getGuildMainCharacter(user, 12345L).length());
        assertTrue(DiscordUserHelper.setGuildMainCharacter(user, 12345L, "us", "arthas", "Kugruon"));
        assertFalse(DiscordUserHelper.setGuildMainCharacter(user, 12345L, "us", "arthas", "Kugruonnn"));
        assertEquals(3, DiscordUserHelper.getGuildMainCharacter(user, 12345L).length());
        resultingJson = "{\n" +
                "    \"characters\": [\n" +
                "        {\n" +
                "            \"name\": \"Huulurrah\",\n" +
                "            \"realm\": \"Arthas\",\n" +
                "            \"battlegroup\": \"Ruin\",\n" +
                "            \"class\": 11,\n" +
                "            \"race\": 6,\n" +
                "            \"gender\": 0,\n" +
                "            \"level\": 0,\n" +
                "            \"achievementPoints\": 0,\n" +
                "            \"thumbnail\": \"arthas/149/191471509-avatar.jpg\",\n" +
                "            \"lastModified\": 0\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        user = OAuthHelper.handleCharacterUpdate("us", user, resultingJson);
        userJSON = new JSONObject(user.getJson());
        assertEquals(1,userJSON.getJSONArray("characters").length());

    }
}
