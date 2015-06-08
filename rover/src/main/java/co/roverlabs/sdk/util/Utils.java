package co.roverlabs.sdk.util;

import com.estimote.sdk.Beacon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.roverlabs.sdk.model.Region;

/**
 * Created by SherryYang on 2015-06-05.
 */
public class Utils {


    public static boolean hasIdenticalBeaconMajors(List<Beacon> beacons) {

        int major = beacons.get(0).getMajor();
        for(Beacon beacon : beacons) {
            if(beacon.getMajor() != major) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasIdenticalRegionMajors(Set<Region> regions) {

        List<Region> regionList = new ArrayList<>(regions);
        Integer major = regionList.get(0).getMajor();
        for(Region region : regionList) {
            if(!region.getMajor().equals(major)) {
                return false;
            }
        }
        return true;
    }

    public static Region getRandomRegion(Set<Region> regions) {

        return new ArrayList<Region>(regions).get(0);
    }

    public static Set subtractSet(Set set1, Set set2) {

        Set result = new HashSet(set2);
        result.removeAll(set1);
        return result;
    }
}
