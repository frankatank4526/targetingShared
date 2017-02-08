package FG;
import robocode.*;
import java.awt.geom.Point2D;
import java.util.Random;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * FrankRobot - a robot by (your name here)
 */
public class TargetingBotV3 extends AdvancedRobot
{
 private byte moveDirection = 1;
 private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
 public void run() {
    setAdjustRadarForRobotTurn(true);
    enemy.reset();
    while (true) {
  setTurnRadarRight(360);
  runForrestRun();
  pewPew();
  execute();
    }
 }
 /**
  * run: FrankRobot's default behavior
  */

 /**
  * onScannedRobot: What to do when you see another robot
  */
 public int randInt(int min, int max) {
     Random rand = new Random();
     int randomNum = rand.nextInt((max - min) + 1) + min;
     return randomNum;
 }
 public void runForrestRun(){
  if (getVelocity() == 0){
      moveDirection *= -1;
  } 
  setTurnRight(normalizeBearing(enemy.getBearing() + 90 - (15 * moveDirection)));
     setAhead(enemy.getDistance() * moveDirection);
 }
 public void pewPew(){
  double firePower = Math.min(500 / enemy.getDistance(), 3);
  double bulletSpeed = 20 - firePower * 3;
  long time = (long)(enemy.getDistance() / bulletSpeed);
  double futureX = enemy.getFutureX(time);
  double futureY = enemy.getFutureY(time);
  double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
  setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));
 }
 public void onScannedRobot(ScannedRobotEvent e) {
    // if we have no enemy or we found the one we're tracking..
     if (enemy.none() || e.getDistance() < enemy.getDistance() - 70 
  || e.getName().equals(enemy.getName())) {
         enemy.update(e, this);
   if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10){
       setFire(Math.min(400 / enemy.getDistance(), 3));
   }
   double turn = getHeading() - getGunHeading() + e.getBearing();
   setTurnGunRight(normalizeBearing(turn));
   int scanDirection = 1;
      scanDirection *= -1;
      setTurnRadarRight(360 * scanDirection);
     }
  
 }
 public void onRobotDeath(RobotDeathEvent e) {
    // if the bot we were tracking died..
     if (e.getName().equals(enemy.getName())) {
         // clear his info, so we can track another
         enemy.reset();
     }
 }

 /**
  * onHitByBullet: What to do when you're hit by a bullet
  */
 public void onHitByBullet(HitByBulletEvent e) {
  // Replace the next line with any behavior you would like
  back(10);
 }
 
 /**
  * onHitWall: What to do when you hit a wall
  */
 public void onHitWall(HitWallEvent e) {
  // Replace the next line with any behavior you would like
  back(20);
 }
 public double normalizeBearing(double angle) {
     while (angle >  180) angle -= 360;
     while (angle < -180) angle += 360;
     return angle;
 } 
 public double absoluteBearing(double x1, double y1, double x2, double y2) {
     double xo = x2-x1;
  double yo = y2-y1;
  double hyp = Point2D.distance(x1, y1, x2, y2);
  double arcSin = Math.toDegrees(Math.asin(xo / hyp));
  double bearing = 0;
 
     if (xo > 0 && yo > 0) { // both pos: lower-Left
         bearing = arcSin;
     } else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
         bearing = 360 + arcSin; // arcsin is negative here, actually 360 - ang
     } else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
         bearing = 180 - arcSin;
     } else if (xo < 0 && yo < 0) { // both neg: upper-right
         bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
     }
 
     return bearing;
 }
}