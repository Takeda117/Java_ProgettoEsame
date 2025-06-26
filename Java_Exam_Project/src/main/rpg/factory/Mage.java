package rpg.factory;

import rpg.iterator.Item;
import rpg.iterator.Inventory;
import java.util.Random;

/**
 * Mage character class
 * <p>
 * This class represents a mage character type in the game.
 * It extends AbstractCharacter and implements mage-specific
 * behaviors. Mages have lower health but can use mana for
 * powerful spell attacks.
 * </p>
 */
public class Mage extends AbstractCharacter {

  private static final Random random = new Random();
  int mana;
  int maxMana;
  private final Inventory inventory;

  /**
   * Creates a new Mage
   * 
   * @param name The mage's name
   */
  public Mage(String name) {
    super(name, 80, 120, 10);
    this.mana = 50;
    this.maxMana = 50;
    this.inventory = new Inventory(20);
  }

  /**
   * Performs a mage attack
   * <p>
   * Mages can use mana for stronger spell attacks or
   * perform weaker staff attacks when low on mana.
   * </p>
   * 
   * @return Damage dealt by the attack, or 0 if the attack fails
   */
  @Override
  public int attack() {
    if (stamina < 3) {
      System.out.printf("%s is too tired!%n", name);
      return 0;
    }

    stamina -= 3;

    int equipmentBonus = inventory != null ? inventory.getTotalStatBonus() : 0;
    int baseAttack = baseDamage + equipmentBonus;

    // Mana-based attack logic
    if (mana >= 10) {
      mana -= 10;
      int magicDamage = baseAttack + 5 + random.nextInt(10);
      System.out.printf("%s casts a spell for %d damage! Mana: %d/%d%n",
              name, magicDamage, mana, maxMana);
      return magicDamage;
    } else {
      int staffDamage = baseAttack + random.nextInt(3);
      System.out.printf("%s attacks with staff for %d damage!%n", name, staffDamage);
      return staffDamage;
    }
  }

  /**
   * Implements mage-specific training
   */
  @Override
  protected void performTraining() {
    baseDamage += 1;
    maxMana += 10;
    mana = maxMana;
    maxStamina += 5;
    System.out.printf("%s studies magic!%n", name);
  }

  /**
   * Overrides the rest method to also restore mana
   */
  @Override
  public void rest() {
    super.rest();
    this.mana = maxMana;
    System.out.printf("%s meditates and restores mana!%n", name);
  }

  /**
   * Equips an item to the mage
   * 
   * @param item The item to equip
   */
  @Override
  public void equipItem(Item item) {
    if (item == null || inventory == null || !item.isEquippable()) {
      return;
    }

    if (!inventory.getAllItems().contains(item)) {
      System.out.println("Item not in inventory!");
      return;
    }

    inventory.equipItem(item);
    System.out.printf("%s equipped %s!%n", name, item.getName());
  }

  /**
   * Adds an item to the mage's inventory
   * 
   * @param item The item to add
   */
  @Override
  public void addItem(Item item) {
    if (item == null || inventory == null) {
      return;
    }

    if (inventory.addItem(item)) {
      System.out.printf("%s added %s!%n", name, item.getName());
    }
  }

  /**
   * Shows the mage's inventory
   */
  @Override
  public void showInventory() {
    if (inventory == null) {
      return;
    }

    System.out.printf("\n=== %s's Equipment ===%n", name);
    inventory.displayInventory();

    int totalBonus = inventory.getTotalStatBonus();
    if (totalBonus > 0) {
      System.out.printf("Magic bonus: +%d power%n", totalBonus);
    }
    System.out.printf("Mana: %d/%d%n", mana, maxMana);
  }

  /**
   * Gets the mage's stamina recovery rate
   * 
   * @return The stamina recovery rate as a decimal
   */
  public double getStaminaRecoveryRate() {
    return 0.10;
  }

  /**
   * Gets current mana
   * 
   * @return Current mana amount
   */
  public int getMana() {
    return mana;
  }

  /**
   * Gets maximum mana
   * 
   * @return Maximum mana amount
   */
  public int getMaxMana() {
    return maxMana;
  }

  /**
   * Returns a string representation of the mage
   * 
   * @return A formatted string with the mage's stats
   */
  @Override
  public String toString() {
    int totalDamage = baseDamage;
    if (inventory != null) {
      totalDamage += inventory.getTotalStatBonus();
    }

    return String.format("Mage %s [HP: %d/%d, Stamina: %d/%d, Mana: %d/%d, Power: %d, Money: %d, Level: %d]",
            name, health, maxHealth, stamina, maxStamina,
            mana, maxMana, totalDamage, money, level);
  }
}
