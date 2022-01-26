## Part One: Get Your Game On

The player has an inventory of weapons and potions. These can affect a player's health. For debugging purposes, we can also fetch a summary of a player's inventory.

```java
public class Potion {
    private final String name;
    private final int healAmount;

    public Potion(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    public String getName() {
        return name;
    }
    public int getHealAmount() {
        return healAmount;
    }

    public void quaff(Player target) {
        target.modifyHealth(healAmount);
    }
}

public class Weapon {
    private final String name;
    private final int attackPower;

    public Weapon(String name, int attackPower) {
        this.name = name;
        this.attackPower = attackPower;
    }

    public String getName() {
        return name;
    }
    public int getAttackPower() {
        return attackPower;
    }

    public void attack(Player target) {
        target.modifyHealth(-attackPower);
    }
}

public class Player {
    private final String name;
    private int health = 100;
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getHealth() {
        return health;
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }
    public void addPotion(Potion potion) {
        potions.add(potion);
    }

    public void modifyHealth(int amount) {
        health += amount;
        if (health > 100) {
            health = 100;
        }
        if (health < 0) {
            health = 0;
        }
    }

    public List<String> describeInventory() {
        List<String> result = new ArrayList<>();
        for (Weapon weapon : weapons) {
            result.add(String.format(
                "%s - %d ATK", weapon.getName(), weapon.getAttackPower()
            ));
        }
        for (Potion potion : potions) {
            result.add(String.format(
                "%s - heals %d", potion.getName(), potion.getHealAmount()
            ));
        }
        return result;
    }
}

public class Game {
    public static void main(String[] args) {
        Player arthur = new Player("Arthur Pendragon");
        Weapon excalibur = new Weapon("Excalibur", 25);
        Potion minor = new Potion("Minor healing draught", 15);
        Potion senzu = new Potion("Senzu bean", 100);
        arthur.addWeapon(excalibur);
        arthur.addPotion(minor);
        arthur.addPotion(senzu);

        Player cuchulainn = new Player("Cú Chulainn");
        Weapon gaebulg = new Weapon("Gáe Bulg", 30);
        cuchulainn.addWeapon(gaebulg);

        gaebulg.attack(arthur);
        minor.quaff(arthur);

        System.out.println("Arthur health: " + arthur.getHealth());
        for (String descriptor : arthur.describeInventory()) {
            System.out.println(descriptor);
        }
    }
}
```

Run this and the result is:

```
Arthur health: 85
Excalibur - 25 ATK
Minor healing draught - heals 15
Senzu bean - heals 100
```

Right now, this design is _okay_. It could be improved, but nothing is screaming out for abstraction: a lot of developers will follow the "rule of three" where if you repeat yourself twice, that is acceptable, but if you repeat yourself three times, you should abstract.

## Part Two: We Could All Use a Little Change

Our game designers have decided that players need bags to handle inventory management. Bags can contain weapons, potions, and even other bags. They also have a finite capacity.

So we add a `Bag` class:

```java
public class Bag {
    private final String name;
    private final int capacity;
    private int count = 0;
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Bag> bags = new ArrayList<>();

    public Bag(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }
    public int getCapacity() {
        return capacity;
    }
    public int getCount() {
        return count;
    }

    /*
    * These getters return a read-only copy of the list to enforce that you
    * must use the "add" methods below. Ensures nobody can push us past our
    * capacity.
    */
    public List<Weapon> getWeapons() {
        return Collections.unmodifiableList(weapons);
    }
    public List<Potion> getPotions() {
        return Collections.unmodifiableList(potions);
    }
    public List<Bag> getBags() {
        return Collections.unmodifiableList(bags);
    }

    public void incrementCount() {
        if (count == capacity) {
            throw new RuntimeException("Already at capacity");
        }
        count++;
    }
    public void addWeapon(Weapon weapon) {
        incrementCount();
        weapons.add(weapon);
    }
    public void addPotion(Potion potion) {
        incrementCount();
        potions.add(potion);
    }
    public void addBag(Bag bag) {
        incrementCount();
        bags.add(bag);
    }
}
```

In our Player class, we make some modifications, including making `describeInventory` into a recursive call that descends through all the player's bags and shows their contents, indented.

```java
public class Player {
    // ...
    private final List<Bag> bags = new ArrayList<>();

    public void addBag(Bag bag) {
        bags.add(bag);
    }

    public List<String> describeInventory() {
        return describeInventoryInternal("", weapons, potions, bags);
    }

    private static List<String> describeInventoryInternal(
        String indent,
        List<Weapon> weapons,
        List<Potion> potions,
        List<Bag> bags
    ) {
        List<String> result = new ArrayList<>();
        for (Weapon weapon : weapons) {
            result.add(String.format(
                "%s%s - %d ATK", indent, weapon.getName(), weapon.getAttackPower()
            ));
        }
        for (Potion potion : potions) {
            result.add(String.format(
                "%s%s - heals %d", indent, potion.getName(), potion.getHealAmount()
            ));
        }
        for (Bag bag : bags) {
            result.add(String.format(
                "%s%s - (%d/%d)", indent, bag.getName(), bag.getCount(), bag.getCapacity()
            ));
            result.addAll(
                describeInventoryInternal(
                    indent + "  ",
                    bag.getWeapons(),
                    bag.getPotions(),
                    bag.getBags()
                )
            );
        }
        return result;
    }
}
```

Our `Game` class can now look like:

```java
public class Game {
    public static void main(String[] args) {
        Player arthur = new Player("Arthur Pendragon");
        Weapon excalibur = new Weapon("Excalibur", 25);
        arthur.addWeapon(excalibur);

        Player cuchulainn = new Player("Cú Chulainn");
        Weapon gaebulg = new Weapon("Gáe Bulg", 30);
        Bag ninjaBag = new Bag("Ninja's bag", 5);
        Bag kunaiBundle = new Bag("Kunai bundle", 20);
        for (int i = 0; i < 5; i++) {
            kunaiBundle.addWeapon(new Weapon("Kunai", 5));
        }
        ninjaBag.addBag(kunaiBundle);
        Potion poison = new Potion("Nightshade poison", -100);
        ninjaBag.addPotion(poison);
        cuchulainn.addWeapon(gaebulg);
        cuchulainn.addBag(ninjaBag);

        excalibur.attack(cuchulainn);
        poison.quaff(cuchulainn);

        System.out.println("Cú Chulainn health: " + cuchulainn.getHealth());
        for (String descriptor : cuchulainn.describeInventory()) {
            System.out.println(descriptor);
        }
    }
}
```

And when we run it:

```
Cú Chulainn health: 0
Gáe Bulg - 30 ATK
Ninja's bag - (2/5)
  Nightshade poison - heals -100
  Kunai bundle - (5/20)
    Kunai - 5 ATK
    Kunai - 5 ATK
    Kunai - 5 ATK
    Kunai - 5 ATK
    Kunai - 5 ATK
```

## Part Three: They Don't Stop Coming

We're starting to see some real maintenance nightmares on the horizon now. We listened in on a conversation with the game designers, and we're hearing that there are several changes to be proposed soon:

* Bag capacity should be based on weight, not just item count. Which means that everything that can be in a bag will need weight to be tracked.
* Everything in a player's inventory should also have a color that the player can set to organize things at a glance.
* They want to add armor that can reduce damage from attacks. Also wands that can cast spells.

Now we're starting to repeat ourselves _everywhere_. We will need to add classes for `Armor` and `Wand`, but we also need to modify code in `Player` and `Bag` to handle them. `Armor`, `Wand`, `Bag`, `Weapon`, and `Potion` will all need code to handle weight and color.

So before those requests even come in, we're going to refactor our code, abstract out some of the shared logic.

First, we introduce a new `Item` abstract class as the superclass of things that can be in an inventory:

```java
public abstract class Item {
    private final String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

This is the class that will eventually handle even more shared concerns like weight and color.

Then we modify `Weapon`, `Potion`, and `Bag` to extend that class. Now, anywhere that we had a `List<Weapon>`, `List<Potion>`, `List<Bag>`... we can just have a `List<Item>`.

So now our `Item` subclasses look like:

```java
public class Potion extends Item {
    private final int healAmount;

    public Potion(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public void quaff(Player target) {
        target.modifyHealth(healAmount);
    }
}

public class Weapon extends Item {
    private final int attackPower;

    public Weapon(String name, int attackPower) {
        super(name);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void attack(Player target) {
        target.modifyHealth(-attackPower);
    }
}

public class Bag extends Item {
    private final int capacity;
    private int count = 0;
    private final List<Item> contents = new ArrayList<>();

    public Bag(String name, int capacity) {
        super(name);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
    public int getCount() {
        return count;
    }

    /*
    * This getter returns a read-only copy of the list to enforce that you
    * must use the "add" method below. Ensures nobody can push us past our
    * capacity.
    */
    public List<Item> getContents() {
        return Collections.unmodifiableList(contents);
    }

    public void addItem(Item item) {
        if (count == capacity) {
            throw new RuntimeException("Already at capacity");
        }
        count++;
        contents.add(item);
    }
}
```

These are already a lot simpler and less repetitive. Finally, we modify `Player`:

```java
public class Player {
    private final List<Item> inventory = new ArrayList<>();

    public void addItem(Item item) {
        inventory.add(item);
    }

    public List<String> describeInventory() {
        return describeInventoryInternal("", inventory);
    }

    private static List<String> describeInventoryInternal(String indent, List<Item> items) {
        List<String> result = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                result.add(String.format(
                    "%s%s - %d ATK", indent, weapon.getName(), weapon.getAttackPower()
                ));
            }
            else if (item instanceof Potion) {
                Potion potion = (Potion) item;
                result.add(String.format(
                    "%s%s - heals %d", indent, potion.getName(), potion.getHealAmount()
                ));
            }
            else if (item instanceof Bag) {
                Bag bag = (Bag) item;
                result.add(String.format(
                    "%s%s - (%d/%d)", indent, bag.getName(), bag.getCount(), bag.getCapacity()
                ));
                result.addAll(
                    describeInventoryInternal(indent + "  ", bag.getContents())
                );
            }
        }
        return result;
    }
}
```

So, now because we're using a *polymorphic type* `Item`, instead of having multiple collections to deal with different kinds of items, we deal with them all together. Run the `Game` class and we see the only noticeable change is the order of the inventory report, because now we show everything in the order it was added, rather than implicitly grouping by type. But that's okay: we had no requirements on the ordering of this information.

```
Cú Chulainn health: 0
Gáe Bulg - 30 ATK
Ninja's bag - (2/5)
  Kunai bundle - (5/20)
    Kunai - 5 ATK
    Kunai - 5 ATK
    Kunai - 5 ATK
    Kunai - 5 ATK
    Kunai - 5 ATK
  Nightshade poison - heals -100
```

There's one more improvement we can do though.

## Part Four: You Hit the Ground Running

Now if the designers want to add weight or color, we're set: we only have to add those in one place. And if they want to add armor or wands, we've made things _better_, because we don't need to add new collections for those types and we don't need to modify the `Bag` class at all. But we still have to modify `Player` because `describeInventoryInternal` is still aware of each of the item types.

This is a pretty good example of a "code smell": any time you see conditional behavior that's based on types, it's _usually_ something that can be improved with polymorphism.

In this case, `describeInventoryInternal` is going through each item and, based on its type, adding one to several strings to a list. Instead of using a conditional like this, we can pull this functionality into the classes themselves by using an abstract method on `Item`:

```java
public abstract class Item {
    private final String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract List<String> getDescriptors(String indent);
}
```

And then we just need to implement that method on each subclass:

```java
public class Potion extends Item {
    @Override
    public List<String> getDescriptors(String indent) {
        return Collections.singletonList(
            String.format("%s%s - heals %d", indent, getName(), healAmount)
        );
    }
}

public class Weapon extends Item {
    @Override
    public List<String> getDescriptors(String indent) {
        return Collections.singletonList(
            String.format("%s%s - %d ATK", indent, getName(), attackPower)
        );
    }
}

public class Bag extends Item {
    @Override
    public List<String> getDescriptors(String indent) {
        List<String> result = new ArrayList<>();
        result.add(String.format("%s%s - (%d/%d)", indent, getName(), count, capacity));
        for (Item item : contents) {
            result.addAll(item.getDescriptors(indent + "  "));
        }
        return result;
    }
}
```

And modify `Player` to use it:

```java
public class Player {
    public List<String> describeInventory() {
        List<String> result = new ArrayList<>();
        for (Item item : inventory) {
            result.addAll(item.getDescriptors(""));
        }
        return result;
    }
}
```

Now, when the designers want to add `Armor` or `Wand`, we only need to add classes for those types, and we don't need to touch _anything_ in `Player` or `Bag` to allow them to function correctly in the inventory system.

Now, there's still some repetition in `getDescriptors`. Every subclass includes `%s%s - ` at the start of its format string and fills in `indent` and `getName()` to those fields. So I considered pulling this functionality into `Item` – but because of `Bag` emitting multiple lines, the complexity introduced by this abstraction was unlikely to be worth it.

## Conclusion: You're Bundled Up Now

So what was the point of the tutorial being so long and going through each of these steps, why not just show the final code in step 4?

Well, that's what makes something a _tutorial_ and not just _reference documentation_. If you just need to know how to declare class inheritance in a language, just what it looks like, you can look up the language reference for that.

The point of going through each of these steps is to understand:

1. What problem is this abstraction going to solve?
2. What are the benefits of using this abstraction?
3. What are the drawbacks of it?

OOP is, at the most basic, a set of techniques for software abstraction. These are the skills required to be effective at using it.