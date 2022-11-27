package net.messer.mystical_index.util.request;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class QueryBasedRequest extends Request {
    protected static final String QUESTION_MARK = "?";
    private static final Pattern STACKS_MATCHER = Pattern.compile("(?<amount>\\d+|all) stacks?( of)? (?<item>.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern STACK_MATCHER = Pattern.compile("stack( of)? (?<item>.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern COUNTS_MATCHER = Pattern.compile("(?<amount>\\d+|all)x? (?<item>.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SINGLE_MATCHER = Pattern.compile("(?<item>.+)", Pattern.CASE_INSENSITIVE); // TODO rework this
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

        QueryBasedRequest request = QueryBasedRequest.parseAmount(STACKS_MATCHER.matcher(query), (integer, item) -> integer * item.getMaxCount());
        if (request != null) return request;
        request = QueryBasedRequest.parseAmount(STACK_MATCHER.matcher(query), (integer, item) -> item.getMaxCount());
        if (request != null) return request;
        request = QueryBasedRequest.parseAmount(COUNTS_MATCHER.matcher(query), (integer, item) -> integer);
        if (request != null) return request;
        request = QueryBasedRequest.parseAmount(SINGLE_MATCHER.matcher(query), (integer, item) -> 1);
        return request;
    }

    private static ExtractionRequest parseAmount(Matcher matcher, BiFunction<Integer, Item, Integer> amountModifier) {
        if (matcher.matches()) {
            int amount;
            try {
                amount = Integer.parseInt(matcher.group("amount"));
            } catch (NumberFormatException e) {
                amount = Integer.MAX_VALUE;
            } catch (IllegalArgumentException e) {
                amount = 1;
            }

            return new ExtractionRequest(matcher.group("item"), amount, amountModifier);
        }
        return null;
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

        return matchGlob(expression, itemName)
                || matchGlob(expression, itemName + "s")
                || matchGlob(expression, itemName + "es")
                || itemName.endsWith("y") && matchGlob(expression, itemName.substring(0, itemName.length() - 1) + "ies");
    }

    public String getFullQuery() {
        return fullQuery;
    }

    public List<ItemStack> getReturnedStacks() {
        return List.of();
    }

    public abstract Text getMessage();
}
