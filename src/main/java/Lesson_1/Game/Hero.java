package Lesson_1.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

abstract class Hero {

    protected int health;
    protected String name;
    protected int damage;
    protected int addHeal;

    public Hero(int health, String name, int damage, int addHeal) {
        this.health = health;
        this.name = name;
        this.damage = damage;
        this.addHeal = addHeal;
    }

    // метод для удара
    abstract void hit(Hero hero);

    // метод для лечения
    abstract void healing(Hero hero);

    // метод для нанесения урона
    void causeDamage(int damage) {
        if(health < 0) {
            System.out.println("Герой уже мертвый!");
        } else {
            health -= damage;
        }

    }

    boolean dead() { return health<0; }

    public int getHealth() {
        return health;
    }

    void addHealth(int health) {
        this.health += health;
    }

    void info() {

        System.out.println(name + " " + (health < 0 ? "Герой мертвый" : health) + " " + damage);
    }
}

class Warrior extends Hero {

    public Warrior(int health, String type, int damage, int addHeal) {
        super(health, type, damage, addHeal);
    }

    @Override
    void hit(Hero hero) {
        if (hero != this) {
            if(health < 0) {
                System.out.println("Герой погиб и бить не может!");
            } else {
                hero.causeDamage(damage);
            }
            System.out.println(this.name +" ["+ this.getHealth()+"] нанес урон <"+ damage+"> " + hero.name+" ["+hero.getHealth()+"]");
        }
    }

    @Override
    void healing(Hero hero) {
        System.out.println("Войны не умеют лечить!");
    }
}

class Assasin extends Hero {
    // критический урон
    int cricitalHit;
    Random random = new Random();

    public Assasin(int heal, String name, int damage, int addHeal) {
        super(heal, name, damage, addHeal);
        this.cricitalHit = random.nextInt(20);
    }

    @Override
    void hit(Hero hero) {
        if (hero != this) {
            if(health < 0) {
                System.out.println("Герой погиб и бить не может!");
            } else {
                hero.causeDamage(damage + cricitalHit);
            }
            System.out.println(this.name +" ["+ this.getHealth()+"] нанес урон <"+ damage+"> " + hero.name+" ["+hero.getHealth()+"]");
        }
    }

    @Override
    void healing(Hero hero) {
        System.out.println("Убийцы не умеют лечить!");
    }
}

class Doctor extends Hero {

    public Doctor(int heal, String name, int damage, int addHeal) {
        super(heal, name, damage, addHeal);
    }

    @Override
    void hit(Hero hero) {
        System.out.println("Доктор не может бить!");
    }

    @Override
    void healing(Hero hero) { // must be aware that hero can be null
        if (hero!=null) {
            System.out.println("доктор "+name+" подлечил "+hero.name+" ["+hero.getHealth()+"]");
            hero.addHealth(addHeal);
        }
    }
}


class Team {
    private String name;
    private ArrayList<Hero> heroes;
    private Random randomHeroGenerator = new Random();

    Team(String name, Hero ... heroes) {
        this.name = name;
        this.heroes = new ArrayList<>(Arrays.asList(heroes));
    }

    boolean anyMemberIsAlive() {
        for (Hero hero : heroes) {
            if (!hero.dead())
                return true;
        }
        return false;
    }

    Hero randomHero() {
        return heroes.get(randomHeroGenerator.nextInt(heroes.size()));
    }

    Hero bestCandidateToHeal() {
        if (heroes.isEmpty())
            throw new RuntimeException("No heros in team");
        int minHealth = heroes.get(0).getHealth();
        Hero retVal = null;
        for (Hero hero: heroes) {
            if (hero.getHealth()<minHealth && !hero.dead()) {
                retVal = hero;
                minHealth = hero.getHealth();
            }
        }
        return retVal;
    }

    boolean turn(Team otherTeam) {
        for (Hero hero: heroes) {
            if (hero instanceof Doctor) {
                hero.healing(bestCandidateToHeal());
            } else {
                hero.hit(otherTeam.randomHero());
            }
        }
        return true;
    }

    void info() {
        for (Hero h : heroes) {
            h.info();
        }
    }
}

class Game {
    public static void main(String[] args) {

        Random randomStep = new Random();
        Random randomHealing = new Random();
        int round = 3;

        Team thunderTeam = new Team("thunder", new Warrior(250, "Тигрил", 50, 0)
                , new Assasin(150, "Акали", 70, 0)
                , new Doctor(120, "Жанна", 0, 60));

        Team optimusTeam = new Team("optimus", new Warrior(290, "Минотавр", 60, 0)
                , new Assasin(160, "Джинкс", 90, 0)
                , new Doctor(110, "Зои", 0, 80));


        boolean thunderTeamTurn = true;
        while (thunderTeam.anyMemberIsAlive() && optimusTeam.anyMemberIsAlive()) {
            if (thunderTeamTurn) {
                System.out.println("-------------------------- THUNDER --------------------------");
                thunderTeam.turn(optimusTeam);
            } else {
                System.out.println("-------------------------- OPTIMUS --------------------------");
                optimusTeam.turn(thunderTeam);
            }
            thunderTeamTurn = !thunderTeamTurn;
        }


        System.out.println("-------------------------- THUNDER --------------------------");
        thunderTeam.info();
        System.out.println("-------------------------- OPTIMUS --------------------------");
        optimusTeam.info();
    }
}
