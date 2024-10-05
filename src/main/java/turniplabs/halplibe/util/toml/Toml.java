package turniplabs.halplibe.util.toml;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Toml {
    protected HashMap<String, Toml> categories = new HashMap<>();
    protected HashMap<String, Entry<?>> entries = new HashMap<>();
    @SuppressWarnings("all")
    protected Optional<String> comment = Optional.empty();

    ArrayList<String> orderedKeys = new ArrayList<>();
    ImmutableList<String> immutKeys = null;

    public Object get(String key) {
        return get(key, Object.class);
    }

    @SuppressWarnings({"unchecked", "unused", "DuplicateExpressions"})
    public <T> T get(String key, Class<T> clazz) {
        if (key.startsWith(".")) {
            if (key.substring(1).contains(".")) {
                String[] split = key.substring(1).split("\\.", 2);
                return (T) categories.get(split[0]).get("." + split[1]);
            }
            return (T) categories.get(key.substring(1));
        } else if (key.contains(".")) {
            String[] split = key.split("\\.", 2);
            return (T) categories.get(split[0]).get(split[1]);
        } else {
            Entry<?> entry = entries.get(key);
            if (entry == null) return null;

            Object value = entry.getT();

            if (value instanceof String) {
                if (clazz.equals(Byte.class) || clazz.equals(byte.class))
                    return (T) (Byte) Byte.parseByte((String) value);
                if (clazz.equals(Short.class) || clazz.equals(short.class))
                    return (T) (Short) Short.parseShort((String) value);
                if (clazz.equals(Integer.class) || clazz.equals(int.class))
                    return (T) (Integer) Integer.parseInt((String) value);
                if (clazz.equals(Long.class) || clazz.equals(long.class))
                    return (T) (Long) Long.parseLong((String) value);

                if (clazz.equals(Float.class) || clazz.equals(float.class))
                    return (T) (Float) Float.parseFloat((String) value);
                if (clazz.equals(Double.class) || clazz.equals(double.class))
                    return (T) (Double) Double.parseDouble((String) value);

                if (clazz.equals(Boolean.class) || clazz.equals(boolean.class))
                    return (T) (Boolean) Boolean.parseBoolean((String) value);
            } else {
                if (clazz.equals(Float.class) || clazz.equals(float.class))
                    return (T) (Float) ((Number) value).floatValue();
                if (clazz.equals(Double.class) || clazz.equals(double.class))
                    return (T) (Double) ((Number) value).doubleValue();
                if (clazz.equals(Long.class) || clazz.equals(long.class))
                    return (T) (Long) (((Number) value).longValue());
                if (clazz.equals(Integer.class) || clazz.equals(int.class))
                    return (T) (Integer) (((Number) value).intValue());
                if (clazz.equals(Short.class) || clazz.equals(short.class))
                    return (T) (Short) (((Number) value).shortValue());
                if (clazz.equals(Byte.class) || clazz.equals(byte.class))
                    return (T) (Byte) (((Number) value).byteValue());
            }

            return (T) value;
        }
    }

    public Entry<?> getEntry(String key) {
        if (key.startsWith(".")) {
            throw new RuntimeException("Categories are not entries");
        } else {
            return entries.get(key);
        }
    }

    public Toml() {
    }

    public Toml(String comment) {
        this.comment = Optional.of(comment);
    }
    @CanIgnoreReturnValue
    public Toml addCategory(String comment, String name) {
        return addCategory(name, new Toml(comment));
    }
    @CanIgnoreReturnValue
    public Toml addCategory(String name) {
        return addCategory(name, new Toml());
    }

    protected Toml addCategory(String name, Toml category) {
        if (name.contains(".")) {
            String[] split = name.split("\\.", 2);
            Toml toml = categories.get(split[0]);
            if (toml == null) {
                categories.put(split[0], toml = new Toml());
                orderedKeys.add("." + split[0]);
            }
            toml.addCategory(split[1], category);
        } else {
            orderedKeys.add("." + name);
            categories.put(name, category);
        }
        return category;
    }
    @CanIgnoreReturnValue
    public <T> Toml addEntry(String name, T value) {
        return addEntry(name, new Entry<>(value));
    }
    @CanIgnoreReturnValue
    public <T> Toml addEntry(String name, String comment, T value) {
        return addEntry(name, new CommentedEntry<>(comment, value));
    }

    public Toml addEntry(String name, Entry<?> value) {
        immutKeys = null;

        if (name.contains(".")) {
            String[] split = name.split("\\.", 2);
            Toml toml = categories.get(split[0]);
            if (toml == null) {
                categories.put(split[0], toml = new Toml());
                orderedKeys.add("." + split[0]);
            }
            toml.addEntry(split[1], value);
        } else if (entries.put(name, value) == null)
            orderedKeys.add(name);
        return this;
    }
    @SuppressWarnings("unchecked")
    public ImmutableList<String> getOrderedKeys() {
        if (immutKeys == null)
            // java generics are stupid...
            immutKeys = (ImmutableList<String>) (ImmutableList<?>) ImmutableList.builder().addAll(orderedKeys).build();
        return immutKeys;
    }

    protected String repeat(String txt, int count) {
        StringBuilder dst = new StringBuilder();
        for (int i = 0; i < count; i++) dst.append(txt);
        return dst.toString();
    }

    public String toString(String rootKey, int indents) {
        StringBuilder builder = new StringBuilder();

        if (rootKey.isEmpty()) {
            if (this.comment.isPresent()) {
                String comment = this.comment.get();

                for (String re : comment.split("\n"))
                    builder.append(repeat("\t", indents)).append("# ").append(re).append("\n");
            }

            builder.append("\n");
        }

        for (String orderedKey : getOrderedKeys()) {
            String[] res;
            int offset = 0;
            int sep = 0;

            if (orderedKey.startsWith(".")) {
                if (orderedKey.substring(1).contains(".")) continue;

                Toml cat = categories.get(orderedKey.substring(1));
                String full = rootKey + (rootKey.isEmpty() ? "" : ".") + orderedKey.substring(1);

                if (cat.comment.isPresent()) {
                    String comment = cat.comment.get();

                    for (String re : comment.split("\n"))
                        builder.append(repeat("\t", indents)).append("# ").append(re).append("\n");
                }

                builder.append(repeat("\t", indents)).append("[").append(full).append("]").append("\n");

                res = cat.toString(full, 0).split("\n");
                sep = offset = 1;
            } else {
                res = entries.get(orderedKey).toString(orderedKey).split("\n");
            }

            for (String re : res) builder.append(repeat("\t", indents + offset)).append(re).append("\n");
            builder.append(repeat("\n", sep));
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return toString("", 0);
    }
    @SuppressWarnings("unused") // API function
    public boolean contains(String entry) {
        return get(entry) != null;
    }

    public Optional<String> getComment() {
        return comment;
    }

    public void merge(boolean complete, Toml parsed) {
        for (String orderedKey : parsed.orderedKeys) {
            if (orderedKey.startsWith(".")) {
                orderedKey = orderedKey.substring(1);
                if (complete) {
                    if (!categories.containsKey(orderedKey))
                        addCategory(orderedKey);
                    categories.get(orderedKey).merge(complete, parsed.categories.get(orderedKey));
                } else if (categories.containsKey(orderedKey))
                    categories.get(orderedKey).merge(complete, parsed.categories.get(orderedKey));
            } else {
                if (complete) {
                    if (entries.containsKey(orderedKey)) {
                        Entry<?> entry = entries.get(orderedKey);
                        if (entry instanceof CommentedEntry) {
                            entries.replace(orderedKey, new CommentedEntry<>(
                                    ((CommentedEntry<?>) entry).getComment(),
                                    parsed.getEntry(orderedKey).t
                            ));
                        } else {
                            entries.replace(orderedKey, parsed.getEntry(orderedKey));
                        }
                    } else {
                        entries.replace(orderedKey, parsed.getEntry(orderedKey));
                    }
                } else if (!this.entries.containsKey(orderedKey)) {
                    entries.replace(orderedKey, parsed.getEntry(orderedKey));
                }
            }
        }
    }

    public void merge(Toml parsed) {
        merge(false, parsed);
    }

    public void addMissing(Toml other) {
        for (String orderedKey : other.getOrderedKeys()) {
            if (orderedKey.startsWith(".")) {
                if (!this.categories.containsKey(orderedKey)) {
                    Toml toml = other.get(orderedKey, Toml.class);
                    if (toml.getComment().isPresent())
                        addCategory(toml.getComment().get(), orderedKey.substring(1));
                    else addCategory(orderedKey.substring(1));
                    categories.get(orderedKey.substring(1)).addMissing(toml);
                }
            } else {
                if (!this.entries.containsKey(orderedKey)) {
                    addEntry(orderedKey, other.getEntry(orderedKey));
                }
            }
        }
    }

    public void remove(String s) {
        immutKeys = null;

        if (s.startsWith(".")) {
            if (s.substring(1).contains(".")) {
                categories.get(s.substring(1).split("\\.")[0])
                        .remove("." + s.substring(1).split("\\.")[1]);
                return;
            }
            orderedKeys.remove(s);
            categories.remove(s.substring(1));
        } else {
            if (s.contains(".")) {
                categories.get(s.split("\\.")[0])
                        .remove(s.split("\\.")[1]);
                return;
            }
            orderedKeys.remove(s);
            entries.remove(s);
        }
    }
}
