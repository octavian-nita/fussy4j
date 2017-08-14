package eu.fuss4j.algo;

import eu.fuss4j.MatchFn;
import eu.fuss4j.rang.MatchWithRanges;
import eu.fuss4j.rang.Range;

import java.util.Locale;
import java.util.Optional;

import static eu.fuss4j.algo.SubstringMatchFn.Occurrence.ANY;
import static eu.fuss4j.rang.MatchWithRanges.withRanges;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 14, 2017
 */
public class SubstringMatchFn implements MatchFn<String, String, MatchWithRanges<String>> {

    public enum Occurrence {PREFIX, SUFFIX, PREFIX_AND_SUFFIX, ANY}

    protected Occurrence occurrence;

    protected boolean caseSensitive;

    public SubstringMatchFn() { this(ANY, false); }

    public SubstringMatchFn(Occurrence occurrence) { this(occurrence, false); }

    public SubstringMatchFn(boolean caseSensitive) { this(ANY, caseSensitive); }

    public SubstringMatchFn(Occurrence occurrence, boolean caseSensitive) {
        this.occurrence = occurrence == null ? ANY : occurrence;
        this.caseSensitive = caseSensitive;
    }

    public Occurrence getOccurrence() { return occurrence; }

    public boolean isCaseSensitive() { return caseSensitive; }

    protected Locale getLocale() { return Locale.getDefault(); }

    protected void setOccurrence(Occurrence occurrence) { this.occurrence = occurrence; }

    protected void setCaseSensitive(boolean caseSensitive) { this.caseSensitive = caseSensitive; }

    @Override
    public Optional<MatchWithRanges<String>> match(String item, String pattern) {
        if (item == null || pattern == null) {
            return empty();
        }

        final Locale locale = getLocale();
        if (!caseSensitive) {
            item = item.toLowerCase(locale);
            pattern = pattern.toLowerCase(locale);
        }

        MatchWithRanges<String> match = null;

        switch (occurrence) {

        case PREFIX:
            if (item.startsWith(pattern)) {
                match = withRanges(item, new Range(0, pattern.length()));
            }
            break;

        case SUFFIX:
            if (item.endsWith(pattern)) {
                match = withRanges(item, new Range(item.length() - pattern.length(), item.length()));
            }
            break;

        case PREFIX_AND_SUFFIX:
            if (item.startsWith(pattern) && item.endsWith(pattern)) {
                match = withRanges(item, new Range(0, pattern.length()),
                                   new Range(item.length() - pattern.length(), item.length()));
            }
            break;

        case ANY:
            int start = item.indexOf(pattern);
            if (start >= 0) {
                match = withRanges(item, new Range(start, start + pattern.length()));
            }
            break;
        }

        return ofNullable(match);
    }
}