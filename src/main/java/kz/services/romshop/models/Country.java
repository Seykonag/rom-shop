package kz.services.romshop.models;

import java.util.Arrays;
import java.util.List;

public enum Country {
    BELARUS(Arrays.asList(
            Region.BREST, Region.VITEBSK, Region.GOMEL, Region.GRODNO, Region.MINSK, Region.MINSKIY, Region.MOGILEV
    )),
    GEORGIA(Arrays.asList(
            Region.ABKHAZIA, Region.AJARIA, Region.GURIA, Region.IMERETI, Region.KAKHETI, Region.KVEMOKARTLI,
            Region.MTSKHETAMTIANETI, Region.RACHALECHKUMIANDKVEMOSVANET, Region.SAMEGRELOZEMOSVANETI,
            Region.SAMSKHEJAVAKHETI, Region.SHIDAKARTLI, Region.TBILISI
    )),
    KAZAKHSTAN(Arrays.asList(
            Region.AKMOLINSKIY, Region.AKTUBINSKIY, Region.ALMATINSKIY, Region.ALMATY, Region.ASTANA,
            Region.ATIRAYSKIY, Region.BAIKONYR, Region.ORIENTALKAZAKHSTAN, Region.JAMBYLSKIY,
            Region.WESTKAZAKHSTAN, Region.KARAGANDINSKIY, Region.KOSTANASKIY, Region.KYZILORDINSKIY,
            Region.MANGISTAYSKIY, Region.PAVLODARSKIY, Region.NORTHKAZAKHSTAN, Region.SOUTHKAZAKHSTAN
    )),
    KYRGYZSTAN(Arrays.asList(
            Region.BATKEN, Region.BISHKEK, Region.CHU, Region.JALALABAD, Region.NARYN, Region.OSH,
            Region.TALAS, Region.YSYKKOL
    )),
    RUSSIA(Arrays.asList(
            Region.BELGOROD, Region.BRYANSK, Region.VLADIMIR, Region.VOLGOGRAD, Region.VOLOGDA,
            Region.VORONEZH, Region.IVANOVO, Region.IRKUTSK, Region.KALININGRAD, Region.KALUGA,
            Region.KEMEROVO, Region.KURGAN, Region.KURSK, Region.LENINGRAD, Region.LIPETSK,
            Region.MAGADAN, Region.MOSCOW, Region.MURMANSK, Region.NIZHNYNOVGOROD, Region.NOVGOROD,
            Region.NOVOSIBIRSK, Region.OMSK, Region.ORENBURG, Region.OREL, Region.PENZA, Region.PSKOV,
            Region.ROSTOV, Region.RYAZAN, Region.SAMARA, Region.SARATOV, Region.SAKHALIN, Region.SVERDLOVSK,
            Region.SMOLYANINOVSKY, Region.TAMBOV, Region.TVER, Region.TOMSK, Region.TULA, Region.TYUMEN,
            Region.ULYANOVSK, Region.CHELYABINSK, Region.CHECHNYA, Region.CHUVASHIA, Region.CHUKOTKA,
            Region.YAMALNENETS, Region.YAROSLAVL
    )),
    UZBEKISTAN(Arrays.asList(
            Region.ANDIJON, Region.BUXORO, Region.FARGONA, Region.JIZZAX, Region.NAMANGAN, Region.NAVOIY,
            Region.QUASHDARIO, Region.QORAQALPOGISTON, Region.SAMARQAND, Region.SIRDARYO, Region.SURXONDARYO,
            Region.TOSHKENT, Region.TOSHENTSKIY, Region.XORAZM
    )),
    UKRAINE(Arrays.asList(
            Region.VINNYTSIA, Region.VOLYN, Region.DNIPROPETROVSK, Region.DONETSK, Region.ZHYTOMYR,
            Region.ZAKARPATTIA, Region.ZAPORIZHIA, Region.IVANOFRANKIVSK, Region.KYIVCITY, Region.KYIV,
            Region.KIROVOHRAD, Region.LUGANSK, Region.LVOV, Region.MYKOLAIV, Region.ODESSA, Region.POLTAVA,
            Region.RIVNE, Region.SUMY, Region.TERNOPIL, Region.KHERSON, Region.KHMELNYTSKY, Region.CHERKASY,
            Region.CHERNIVTSI, Region.CHERNIHIV
    ));

    private List<Region> regions;

    Country(List<Region> regions) {
        this.regions = regions;
    }

    public List<Region> getRegions() {
        return regions;
    }
}
