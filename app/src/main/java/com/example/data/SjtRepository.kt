package com.example.data

import com.example.model.CompetencyArea
import com.example.model.CompetencyScore
import com.example.model.ItemAnswer
import com.example.model.ItemEvaluation
import com.example.model.SjtItem
import com.example.model.SjtReport
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object SjtRepository {

    val competencies: List<CompetencyArea> = listOf(
        CompetencyArea(
            id = 0,
            name = "Bizalom és pártatlan meghallgatás",
            practice = "Gyakoroljon egy megfigyelésből és egy nyitott kérdésből álló mondatkezdést. A válasz után hagyjon időt, mielőtt visszakérdez."
        ),
        CompetencyArea(
            id = 1,
            name = "Felelősségvállalás és jóvátétel",
            practice = "Esetmegbeszélésen válasszon ki egy konkrét jóvátételi lépést. Gondolja végig, mi lesz a gyermek feladata, és miben segít a felnőtt."
        ),
        CompetencyArea(
            id = 2,
            name = "Életkorhoz igazított határtartás",
            practice = "Egy visszatérően megszegett szabályhoz tervezzen meg egy előzetes emlékeztetőt, egy kipróbálható alternatívát és egy kapcsolódó következményt."
        ),
        CompetencyArea(
            id = 3,
            name = "Biztonság és gyermekvédelmi reagálás",
            practice = "Szakmai konzultáción gyakorolja a veszélyhelyzet megszakítását, illetve a bántalmazásról szóló közlés nyugodt fogadását és továbbjelzését."
        )
    )

    val items: List<SjtItem> = listOf(
        SjtItem(
            id = 0,
            title = "Eltört váza",
            areaIndex = 0,
            scene = "A nyolcéves Lili egyedül volt a nappaliban, amikor csörömpölést hallott. A földön törött váza és egy labda van. Lili azt mondja, a macska verte le a vázát. Korábban látta már, hogy a gyerek megijed, ha valami eltörik. A szilánkoktól biztonságos távolságban vannak.",
            options = listOf(
                "Biztonságossá tenném a helyszínt, és feltakarítanám a szilánkokat. Utána megmondanám Lilinek, hogy biztosan ő törte el a vázát, és beismerést kérnék tőle.",
                "Emlékeztetném Lilit a benti labdázás szabályára, és estig eltenném a labdát, mert úgy gondolnám, hogy a labdázás miatt tört el a váza.",
                "Feltakarítanám a szilánkokat, megnyugtatnám Lilit, és megbeszélném vele, hol lehet labdázni. A váza ügyére már nem térnék vissza.",
                "Biztonságossá tenném a helyszínt, és feltakarítanám a szilánkokat. Elmondanám Lilinek, mit láttam, és megkérném, hogy a saját szavaival mesélje el, mi történt. Ha vállalná a felelősségét, megbeszélnék vele egy apró jóvátételt."
            ),
            why = "A megfigyelésekből induló kérdés teret ad Lilinek, hogy elmondja, mi történt. Egy rövid jóvátétel segít abban, hogy a beszélgetésnek legyen kézzelfogható folytatása."
        ),
        SjtItem(
            id = 1,
            title = "Eltűnt aprópénz",
            areaIndex = 1,
            scene = "Másodszor tűnik el aprópénz a konyhai tálkából. A tizenegy éves Máté négyszemközt elmondja, hogy ő vitte el, és a büfében költötte el, hogy a többiek mellé ülhessen. A pénz az egyik másik nevelt gyereké volt.",
            options = listOf(
                "Megkérném Mátét, hogy a vacsoránál mindenki előtt mondja el, mit tett. Ott beszélném meg vele, hogyan adná vissza a pénzt.",
                "Megállapodnék Mátéval, hogy a következő zsebpénzéből adja vissza az összeget. A büfében történtekre csak akkor térnék vissza, ha ismét eltűnne pénz.",
                "Négyszemközt megbeszélném Mátéval, kinek és hogyan adná vissza a pénzt. A következő napokban zárt helyen tartanám az aprót.",
                "Megköszönném Máténak, hogy elmondta, mi történt, és méltányos jóvátételt egyeztetnék vele. Azt is megbeszélném vele, hogyan csatlakozhatna a többiekhez pénzköltés nélkül."
            ),
            why = "A négyszemközt egyeztetett jóvátételben Máté maga is részt vesz. A büféhelyzet átbeszélése segít megtervezni, mit tehet hasonló helyzetben."
        ),
        SjtItem(
            id = 2,
            title = "Labda a lakásban",
            areaIndex = 2,
            scene = "A kilencéves Bencénél a hiperaktivitás, túl mozgásosság főként várakozáskor látszik: sokat mocorog, felpattan, nehezen marad egyhelyben. Most megint labdázni kezd a folyosón. A labda majdnem eltalálja a kisebb gyereket. Bence el tudja mondani a szabályt, mégis gyakran megfeledkezik róla, amikor sokáig várakozik.",
            options = listOf(
                "Leállítanám a játékot, majd a hét végéig a kinti labdázást is megtiltanám Bencének, hogy megjegyezze a benti szabályt.",
                "Leállítanám a játékot, és megkérném Bencét, hogy a várakozás végéig üljön le mellém. Legközelebb szóban emlékeztetném a szabályra.",
                "Leállítanám a játékot, és biztonságos játékot kínálnék Bencének. Megbeszélném vele, hogyan játszhatna így legközelebb is.",
                "Leállítanám a játékot, Bencével együtt kijelölném a labda helyét, és megbeszélném vele, milyen mozgás fér bele várakozás közben. Legközelebb szóban emlékeztetném erre."
            ),
            why = "A rövid előzetes jelzés és az elérhető mozgáslehetőség segít Bencének, hogy várakozás közben is alkalmazza a szabályt."
        ),
        SjtItem(
            id = 3,
            title = "Elmaradt beadandó",
            areaIndex = 0,
            scene = "A tizenhárom éves Nóri azt mondta, leadta a beadandóját. A tanár üzenete szerint viszont nincs leadva. Nóri a jegyek miatt mostanában feszült. Egyelőre nem világos, hogy feltöltési hiba történt-e, vagy a feladat sem készült el.",
            options = listOf(
                "A tanár üzenetét annak bizonyítékaként kezelném, hogy Nóri hazudott, és emiatt estére elvenném a telefonját.",
                "Új határidőt kérnék a tanártól, és megmondanám Nórinak, hogy addigra mindenképp készüljön el.",
                "Nórival együtt megnézném a feltöltési felületet. Ha nem találnánk ott a beadandót, közösen megterveznénk a pótlását.",
                "Megmutatnám Nórinak a tanár üzenetét, megkérdezném, mit tud a leadásról, és együtt megnéznénk a feltöltési felületet. Ha elakadt, vagy tartott a következményektől, megbeszélném vele, hogyan kérhetne legközelebb segítséget, és hogyan rendezhetné a beadást."
            ),
            why = "A közös ellenőrzés tisztázza, mi történt a feltöltéssel. A beszélgetés segíthet megérteni, milyen támogatás kell Nórinak a következő beadáshoz."
        ),
        SjtItem(
            id = 4,
            title = "Két eltérő történet",
            areaIndex = 0,
            scene = "A nevelt gyerek azt mondja, hogy a nevelőszülő vér szerinti gyereke kezdte a lökdösődést. A másik gyerek ennek az ellenkezőjét állítja. Ön csak a végét látta. A gyerekek már külön vannak, sérülést nem lát.",
            options = listOf(
                "A saját gyerekem beszámolóját fogadnám el, mert régebb óta ismerem. A nevelt gyerektől várnék bocsánatkérést.",
                "Mindkét gyereket emlékeztetném a lökdösődésre vonatkozó szabályra, és megbeszélném velük, mikor kérhetnének felnőtt segítséget egy vitában.",
                "Külön hallgatnám meg a két gyereket, és mindkettőjükkel átbeszélném, hogyan állíthatnának le legközelebb egy elfajuló vitát.",
                "Külön-külön, azonos nyitott kérdésekkel hallgatnám meg a két gyereket. Elmondanám, mit láttam biztosan. Ha tisztázódna, ki miért felelős, jóvátételt várnék attól, aki lökdösődött."
            ),
            why = "A külön meghallgatás teret ad mindkét beszámolónak. A jóvátétel olyan cselekedethez köthető, amelyről biztos információ áll rendelkezésre."
        ),
        SjtItem(
            id = 5,
            title = "Telefon lefekvés után",
            areaIndex = 2,
            scene = "A tizennégy éves Ági a megállapodás ellenére este az ágyban telefonozik a lefekvés után. Elmondja, hogy egy barátja ilyenkor szokott ráírni, és fél, hogy kimarad a beszélgetésből. Másnap nehezen kel fel.",
            options = listOf(
                "Átnézném Ági privát üzeneteit, és ezek alapján dönteném el, megérdemli-e, hogy másnap használhassa a telefonját.",
                "Több napra elvenném a telefont, majd ugyanazokkal a feltételekkel adnám vissza, ha Ági vállalná a közösen elfogadott szabály betartását.",
                "Betartatnám a közösen elfogadott szabályt, és attól az estétől a közös helyiségben töltenénk a telefont. Később megnézném Ágival, könnyebben kel-e reggel.",
                "Újra megbeszélném Ágival, meddig üzenhetne este, és hogyan jelezhetné a barátjának, mikor lesz újra elérhető. Az egyeztetett időponttól a közös helyiségben töltenénk a telefont, később pedig megnéznénk, bevált-e a megállapodás."
            ),
            why = "Az esti határ mellett Ági kap egy megvalósítható módot a kapcsolattartásra. A közös töltőhely és a későbbi ellenőrzés segít felmérni, bevált-e a megállapodás."
        ),
        SjtItem(
            id = 6,
            title = "A felelősség áthárítása",
            areaIndex = 1,
            scene = "A tizenkét éves Dani engedély nélkül elvitte a kisebb gyerek gördeszkáját, és megsérült az egyik kerék. Dani szerint barátja kérte erre és csak ő használta a deszkát, állítja a kerék már előtte is lötyögött. A tulajdonos másnap használni szeretné a játékot.",
            options = listOf(
                "Megmondanám Daninak, hogy a teljes javítást a zsebpénzéből kell fizetnie, mert engedély nélkül vitte el a deszkát.",
                "Megkérném Danit, hogy kérjen bocsánatot az elvitelért. A kerék javítását én intézném, hogy a tulajdonos másnap használhassa a deszkát.",
                "Danival együtt megnézném a kereket, és megkérném, hogy segítsen a javításban. Megbeszélném vele, hogyan kérhetne legközelebb engedélyt.",
                "Tisztáznám Danival, ki miért felelős, és hogy a kerék már korábban is lötyögött-e. Megkérném, hogy mondja el a tulajdonosnak, mi történt, és egyezzenek meg a javítás teendőinek és költségeinek arányos megosztásában."
            ),
            why = "Dani az engedély nélküli elvitelért akkor is felelősséget vállalhat, ha a kerék sérülésének eredete bizonytalan. A tulajdonossal egyeztetett lépés mindkettőjük szempontját figyelembe veszi."
        ),
        SjtItem(
            id = 7,
            title = "A lezárt partszakasz",
            areaIndex = 3,
            scene = "Kiránduláson a tízéves Zsófi átbújik egy korlát alatt, hogy közelebb menjen a meredek vízparthoz. Egy stabil, száraz, vélhetőleg biztonságos részen áll, hallja Önt, és ugyanazon az úton vissza tud jönni. A többi gyerek figyeli mi történik.",
            options = listOf(
                "Megkérdezném Zsófitól, miért ment át a korláton, és csak a válasza után szólítanám vissza.",
                "Visszaszólítanám Zsófit, majd megmondanám neki, hogy a kirándulás hátralévő részében végig mellettem kell maradnia.",
                "Rövid utasítással visszahívnám Zsófit, és ellenőrizném, hogy mindenki biztonságban van-e. A csoporttal átismételném, meddig szabad elmenni.",
                "Rövid, nyugodt utasítással visszavezetném Zsófit ugyanazon az úton. Biztonságos helyen megbeszélném vele, miért ment át a korláton, és mutatnék neki egy megengedett helyet, ahonnan megnézhetné a vizet."
            ),
            why = "A visszavezetés megszünteti a közvetlen veszélyt. A rövid, helyben történő megbeszélés és a megengedett út megmutatása segít a következő döntésben."
        ),
        SjtItem(
            id = 8,
            title = "Késés a barátok miatt",
            areaIndex = 2,
            scene = "A tizenöt éves Marci fél órával a megbeszélt idő után ér haza. Azt mondja, a barátai cukkolták, amikor indulni akart. Volt nála telefon, de nem jelzett. Most otthon van, biztonságban.",
            options = listOf(
                "A következő találkozón Marci barátai előtt kérném számon a késést, hogy ők is lássák a következményét.",
                "Megkérném Marcit, hogy legközelebb legalább induláskor küldjön üzenetet. A hazaérkezés időpontján nem változtatnék.",
                "Megtartanám az eredeti hazaérkezési időt. Megállapodnék Marcival, mikor és milyen üzenetet küldjön, ha késik, és mi lenne a következménye, ha nem jelezne.",
                "Megbeszélném Marcival, mit mondhatna a barátainak induláskor és hogyan jelezhetne, ha késik. A következő alkalomra együtt választanánk olyan hazautat vagy indulási időt, amellyel időben hazaérhetne."
            ),
            why = "Az indulás előre megtervezése, a barátoknak szóló mondat kipróbálása és a jelzés egyeztetése együtt segíti a határ betartását."
        ),
        SjtItem(
            id = 9,
            title = "Közlés egy felnőttről",
            areaIndex = 3,
            scene = "A tizenkét éves Eszter azt mondja, hogy egy felnőtt, aki másnap ismét vele lenne, fájdalmat okozott neki. Kéri, hogy ne beszéljen róla senkinek. Eszter korábban más, hétköznapi ügyekben visszavonta már, amit mondott.",
            options = listOf(
                "Megígérném Eszternek, hogy a közlés köztünk marad, és nem változtatnék a másnapi találkozón.",
                "Gondoskodnék a másnapi felügyeletről, és leírnám, amit Eszter mondott. A hivatalos jelzéssel megvárnám a másnapi konzultációt.",
                "Nyugodtan meghallgatnám Esztert, és a gyermekvédelmi eljárásrend szerint jeleznék. A másnapi találkozó ügyében megvárnám a visszajelzést.",
                "Nyugodtan meghallgatnám Esztert, elmondanám neki, kinek kell szólnom, hogy segítséget kapjon, és feljegyezném a szavait. Haladéktalanul követném a gyermekvédelmi eljárásrendet, és az illetékesekkel azonnal gondoskodnék a biztonságos másnapi felügyeletről."
            ),
            why = "A nyugodt meghallgatás, a pontos feljegyzés, a haladéktalan jelzés és az azonnali biztonságtervezés egymást kiegészítő lépések."
        )
    )

    fun generateShuffledOrders(
        itemCount: Int = items.size,
        random: Random = Random.Default
    ): List<List<Int>> {
        return List(itemCount) {
            listOf(0, 1, 2, 3).shuffled(random)
        }
    }

    fun formatHungarianDate(date: Date = Date()): String {
        val locale = Locale.Builder().setLanguage("hu").setRegion("HU").build()
        return DateFormat.getDateInstance(DateFormat.LONG, locale).format(date)
    }

    fun evaluateTest(
        participantName: String,
        answers: List<ItemAnswer>,
        orders: List<List<Int>>,
        items: List<SjtItem> = SjtRepository.items,
        competencies: List<CompetencyArea> = SjtRepository.competencies,
        formattedDate: String = formatHungarianDate()
    ): SjtReport {
        require(answers.size == items.size && orders.size == items.size) {
            "A válaszok és sorrendek száma nem egyezik a tételek számával."
        }

        val areaScores = IntArray(competencies.size)
        val areaMaxScores = IntArray(competencies.size)

        val itemEvaluations = items.mapIndexed { index, item ->
            val answer = answers[index]
            val bestRank = requireNotNull(answer.bestRank)
            val worstRank = requireNotNull(answer.worstRank)
            require(bestRank != worstRank)

            val bestPoints = bestRank * 2
            val worstPoints = if (worstRank == item.worstKeyRank) 3 else 0
            val itemTotal = bestPoints + worstPoints

            areaScores[item.areaIndex] += itemTotal
            areaMaxScores[item.areaIndex] += 9

            ItemEvaluation(
                itemIndex = index,
                item = item,
                totalPoints = itemTotal,
                bestRank = bestRank,
                bestPoints = bestPoints,
                worstRank = worstRank,
                worstPoints = worstPoints,
                displayOrder = orders[index]
            )
        }

        val competencyProfile = competencies.mapIndexed { idx, area ->
            CompetencyScore(
                area = area,
                score = areaScores[idx],
                maxScore = areaMaxScores[idx]
            )
        }

        val totalScore = itemEvaluations.sumOf { it.totalPoints }
        val maxTotalScore = items.size * 9
        val safetyNeeded = itemEvaluations.getOrNull(7)?.let { it.totalPoints < 9 } == true ||
            itemEvaluations.getOrNull(9)?.let { it.totalPoints < 9 } == true

        val priorities = competencyProfile
            .filter { it.score < it.maxScore }
            .sortedBy { it.ratio }
            .take(2)

        return SjtReport(
            participantName = participantName.trim(),
            formattedDate = formattedDate,
            totalScore = totalScore,
            maxTotalScore = maxTotalScore,
            competencyProfile = competencyProfile,
            needsSafetyConsultation = safetyNeeded,
            priorities = priorities,
            itemEvaluations = itemEvaluations
        )
    }
}
