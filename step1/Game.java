package step1;

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
