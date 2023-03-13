package objectsharing;

import annotation.Immutable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Immutable
public final class Stooges {
    private final Set<String> stooges = new HashSet<>();

    Stooges(String[] names) {
        stooges.addAll(Arrays.asList(names));
    }

    public static void main(String[] args) {
        Stooges stooges = new Stooges(new String[]{"Tom", "Jerry", "Merry"});
        System.out.println(stooges);
        stooges = new Stooges(new String[]{});
        System.out.println(stooges);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append('[');
        for (String stooge : stooges)
            sb.append(stooge).append(',');
        if (sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
        sb.append(']');
        return sb.toString();
    }
}
