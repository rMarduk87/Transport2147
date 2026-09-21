package rpt.games.transport2147.utils.managers

import android.content.Context
import rpt.com.base.log.e
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.appmodels.complex.Profile
import rpt.games.transport2147.utils.data.appmodels.complex.History.addVisitedChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.cleanHistory
import rpt.games.transport2147.utils.data.appmodels.complex.History.requestedChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.size
import rpt.games.transport2147.utils.data.appmodels.complex.History.visitedChapters
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.view.game.PlayerSheet
import rpt.games.transport2147.utils.xml.XmlUtility.getRootElement
import java.util.regex.Matcher
import java.util.regex.Pattern


object ProfileManager {
    fun deleteProfile(context: Context, profile: Profile) {
        try {
            BookManager.deleteProfile(profile)
            val profile2: Profile? = GameLogic.profile as Profile?
            if (profile2 == null || profile.id != profile2.id) {
                return
            }
            GameLogic.profile = null
            GameLogic.playerSheet = null
            cleanHistory()
            GameLogic.mainMenu?.profileLoaded(false, null)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun renameProfile(context: Context, profile: Profile, str: String?): Boolean {
        try {
            val it2: MutableIterator<Profile?> =
                BookManager.getProfiles().iterator()
            while (it2.hasNext()) {
                if (it2.next()!!.name == str) {
                    return false
                }
            }
            val profile2: Profile? = GameLogic.profile as Profile?
            var bool = false
            if (profile2 != null && profile.id == profile2.id) {
                bool = true
            }
            deleteProfile(context, profile)
            profile.setRandomProfileId()
            if (str != null) {
                profile.name = str
            }
            BookManager.addProfile(profile)
            if (bool) {
                loadProfile(context, profile)
            }
            return true
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
            return false
        }
    }

    fun addProfile(context: Context, str: String?, str2: String?) {
        try {
            val profileNewProfile: Profile = Profile.newProfile(str, str2)
            BookManager.addProfile(profileNewProfile)
            loadProfile(context, profileNewProfile)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun loadProfile(context: Context, profile: Profile) {
        try {
            GameLogic.profile = profile
            GameLogic.playerSheet = PlayerSheet(context,
                getRootElement(profile.sheetData)!!)
            cleanHistory()
            requestedChapter = null
            if (!profile.historyData.equals("")) {
                val strArrSplit: List<String> =
                    profile.historyData!!.split(
                        GameConstants.SERIALIZER_SEPARATOR_SEQUENCE)
                for (str in strArrSplit) {
                    addVisitedChapter(str, BookManager.getChapterSummary(str))
                }
                requestedChapter = strArrSplit[strArrSplit.size - 1]
            }
            if (GameLogic.mainMenu != null) {
                GameLogic.mainMenu!!.profileLoaded(true, profile.name)
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun saveProfileHistory(context: Context, profile: Profile?) {
        try {
            val stringBuffer = StringBuffer()
            var z = true
            for (i in 0..<size) {
                val historyElement = visitedChapters[i]
                if (historyElement!!.tracked) {
                    if (!z) {
                        stringBuffer.append(GameConstants.SERIALIZER_SEPARATOR_SEQUENCE)
                    }
                    stringBuffer.append(historyElement.chapter)
                    z = false
                }
            }
            profile!!.historyData = stringBuffer.toString()
            BookManager.updateProfileHistory(profile)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun saveProfileSheet(context: Context, profile: Profile) {
        try {
            profile.sheetData = GameLogic.playerSheet!!.toXml(context)
            BookManager.updateProfileSheet(profile)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun countProfiles(context: Context): Int {
        try {
            return BookManager.getProfiles().size
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
            return 0
        }
    }

    fun packSavedProfiles(context: Context): String? {
        val sb = StringBuilder()
        try {
            for (profile in BookManager.getProfiles()) {
                sb.append(profile!!.name + GameConstants.PROFILE_FIELDS_SEPARATOR +
                        profile.sheetData + GameConstants.PROFILE_FIELDS_SEPARATOR +
                        profile.historyData + GameConstants.PROFILE_PROFILES_SEPARATOR)
            }
            return sb.toString()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
            return null
        }
    }

    fun unpackProfiles(context: Context, str: String?) {
        try {
            val matcher: Matcher =
                Pattern.compile(GameConstants.REGEX_SPLIT_REMOTE_PROFILES).matcher(str)
            val profiles: ArrayList<Profile> = BookManager.getProfiles() as ArrayList<Profile>
            while (matcher.find()) {
                val strGroup = matcher.group(1)
                val strGroup2 = matcher.group(2)
                val strGroup3 = matcher.group(3)
                val profileByName = getProfileByName(profiles, strGroup)
                if (profileByName != null) {
                    deleteProfile(context, profileByName)
                }
                BookManager
                    .addProfile(Profile.newProfile(strGroup, strGroup2, strGroup3))
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun getProfileByName(arrayList: ArrayList<Profile>, str: String?): Profile? {
        for (profile in arrayList) {
            if (profile.name.equals(str)) {
                return profile
            }
        }
        return null
    }
}
