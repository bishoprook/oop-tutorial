package step4;

public class Game {
    public static void main(String[] args) {
        Player arthur = new Player("Arthur Pendragon");
        Weapon excalibur = new Weapon("Excalibur", 25);
        arthur.addItem(excalibur);

        Player cuchulainn = new Player("Cú Chulainn");
        Weapon gaebulg = new Weapon("Gáe Bulg", 30);
        Bag ninjaBag = new Bag("Ninja's bag", 5);
        Bag kunaiBundle = new Bag("Kunai bundle", 20);
        for (int i = 0; i < 5; i++) {
            kunaiBundle.addItem(new Weapon("Kunai", 5));
        }
        ninjaBag.addItem(kunaiBundle);
        Potion poison = new Potion("Nightshade poison", -100);
        ninjaBag.addItem(poison);
        cuchulainn.addItem(gaebulg);
        cuchulainn.addItem(ninjaBag);

        excalibur.attack(cuchulainn);
        poison.quaff(cuchulainn);

        System.out.println("Cú Chulainn health: " + cuchulainn.getHealth());
        for (String descriptor : cuchulainn.describeInventory()) {
            System.out.println(descriptor);
        }
    }
}
