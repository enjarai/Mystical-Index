package net.messer.mystical_index.util.request;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class QueryBasedRequest extends Request {
    protected static final String QUESTION_MARK = "?";
    private static final Pattern STACKS_MATCHER = Pattern.compile("(?<amount>\\d+|all) stacks?( of|) (?<item>.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern MATCHER_STACKS = Pattern.compile("(?<item>.+) (?<amount>\\d+|all) stacks?", Pattern.CASE_INSENSITIVE);
    private static final Pattern STACK_MATCHER = Pattern.compile("stack( of|) (?<item>.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern MATCHER_STACK = Pattern.compile("(?<item>.+) stack", Pattern.CASE_INSENSITIVE);
    private static final Pattern COUNTS_MATCHER = Pattern.compile("(?<amount>\\d+|all)x? (?<item>.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern MATCHER_COUNTS = Pattern.compile("(?<item>.+) (?<amount>\\d+|all)x?", Pattern.CASE_INSENSITIVE);
    private static final Pattern SINGLE_MATCHER = Pattern.compile("(?<item>.+)", Pattern.CASE_INSENSITIVE); // TODO rework this
    private static final List<QueryType> PATTERNS = List.of(
        new QueryType(STACKS_MATCHER, 3, 1, (amount, item) -> amount * item.getMaxCount()),
        new QueryType(MATCHER_STACKS, 1, 2, (amount, item) -> amount * item.getMaxCount()),
        new QueryType(STACK_MATCHER, 1, -1, (amount, item) -> item.getMaxCount()),
        new QueryType(MATCHER_STACK, 1, -1, (amount, item) -> item.getMaxCount()),
        new QueryType(COUNTS_MATCHER, 2, 1, (amount, item) -> amount),
        new QueryType(MATCHER_COUNTS, 1, 2, (amount, item) -> amount),
        new QueryType(SINGLE_MATCHER, 1, -1, (amount, item) -> 1)
    );
    protected final String fullQuery;

    public QueryBasedRequest(int amount, String itemQuery) {
        super(amount);
        this.fullQuery = itemQuery;
    }

    public static QueryBasedRequest get(String query) {
        if (query.endsWith(QUESTION_MARK)) {
            var shortQuery = query.substring(0, query.length() - QUESTION_MARK.length());
            return new ListingRequest(Integer.MAX_VALUE, shortQuery);
        }

//        try {
            for (var info : PATTERNS) {
                var matcher = info.pattern().matcher(query);
                if (matcher.matches()) {
                    var amount = info.amountIndex() != -1 ? matcher.group(info.amountIndex()) : "1";
                    var item = matcher.group(info.itemIndex());
                    var amountInt = amount.equalsIgnoreCase("all") ? Integer.MAX_VALUE / 64 : Integer.parseInt(amount);
                    return new ExtractionRequest(item, amountInt, info.amountModifier());
                }
            }
//        } catch (IllegalArgumentException ignored) {
//        }

        return new ExtractionRequest(query, 1, (amount, item) -> 1);
    }

    protected static boolean matchGlob(String[] expression, String string) {
        if (expression.length == 1) {
            return expression[0].equals(string);
        }

        if (!string.startsWith(expression[0])) {
            return false;
        }

        int offset = expression[0].length();
        for (int i = 1; i < expression.length - 1; i++) {
            String section = expression[i];
            int found = string.indexOf(section, offset);
            if (found == -1) return false;
            offset = found + section.length();
        }
        return string.substring(offset).endsWith(expression[expression.length - 1]);
    }

    protected static boolean itemMatchesExpression(String[] expression, Item item) {
        String itemName = item.toString().toLowerCase(Locale.ROOT).trim();
        String itemCustomName = item.getName().getString().toLowerCase(Locale.ROOT).trim();
        
        return matchMultiplesBlob(expression, itemName)
                || matchMultiplesBlob(expression, itemCustomName);
    }

    protected static boolean matchMultiplesBlob(String[] expression, String query) {
        return matchGlob(expression, query)
                || matchGlob(expression, query + "s")
                || matchGlob(expression, query + "es")
                || query.endsWith("y") && matchGlob(expression, query.substring(0, query.length() - 1) + "ies");
    }

    public String getFullQuery() {
        return fullQuery;
    }

    public List<ItemStack> getReturnedStacks() {
        return List.of();
    }

    public abstract Text getMessage();

    private record QueryType(Pattern pattern, int itemIndex, int amountIndex, BiFunction<Integer, Item, Integer> amountModifier) {}
}
