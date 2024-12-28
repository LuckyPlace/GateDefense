package main;

import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
//gate: 몬스터(enemy)들이 나올 문
public class Gate extends JLabel implements Runnable {
	// 위치
	private int x;
	private int y;

	// 체력
	private int hp;

	// 상수들
	private final static int LL = 100; // `왼쪽 구역`의 `왼쪽 한계`
	private final static int LR = 180; // `왼쪽 구역`의 `오른쪽 한계`
	private final static int RL = 800; // `오른쪽 구역`의 `왼쪽 한계`
	private final static int RR = 900; // `오른쪽 구역`의 `오른쪽 한계`
	private final static int ML = 450; // `가운데 구역`의 `왼쪽 한계`
	private final static int MR = 550; // `가운데 구역`의 `오른쪽 한계`
	private final static int BL = 100; // `1층(바닥)`의 `왼쪽 한계`
	private final static int BR = 900; // `1층(바닥)`의 `오른쪽 한계`

	// image 변수
	private ImageIcon gateImage;

	// 필요한 객체들
	private boolean hit;
	private int xMax;// 랜덤 범위 지정할 때 필요한 변수들
	private int xMin;

	private Player player; // player와 상호작용(좌표 겹치는지 확인)하려면 필요

	public Gate(Player player) { // 생성자
		this.player = player;
		initObject();
		initSetting();
	}

	// getters and setters
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean getHit() {
		return hit;
	}

	public void initObject() {
		gateImage = new ImageIcon("image/gateHp_5.png");
	}

	public void initSetting() {
		// 초기 위치
		x = 600;
		y = 590; // 590이 1층 높이

		// 체력: 5
		hp = 5;
		hit = false;

		setIcon(gateImage);
		setSize(130, 110); // 디폴트 값(의미x)
		setLocation(x, y); // 처음 위치
	}

	public void BeingAttacked() {
		new Thread(() -> {
			while (Math.abs(x - player.getX()) < 150 && Math.abs(y - player.getY()) < 40 && player.isAttack()) {
				try {
					Thread.sleep(170);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				if (hp > 0) {
					System.out.println("[Gate] Being attacked!");
					hp--;
				} else {
					ImageIcon destroy = new ImageIcon("image/gateHp_0.png");
					setIcon(destroy);
				}

				if (this.hp <= 0) { // gate hp 0이 되면 사라져야 함
					System.out.println("[Gate] hp: 0");
					gateImage = new ImageIcon("image/gateHp_0.png");
					setIcon(gateImage);
					try {
						Thread.sleep(500); // 문이 부서지면 0.5초 뒤에 화면에서 사라짐
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 0.5초 뒤 게이트를 화면에서 없앤 후
					setIcon(null);
					// 추가 버그를 방지하기 위해 Thread를 멈춘다.
					break;
				} else if (this.hp == 4) { // 아직 gate 안 부서짐
					System.out.println("[Gate] hp: 4");
					gateImage = new ImageIcon("image/gateHp_4.png");
					setIcon(gateImage);
				} else if (this.hp == 3) { // 아직 gate 안 부서짐
					System.out.println("[Gate] hp: 3");
					gateImage = new ImageIcon("image/gateHp_3.png");
					setIcon(gateImage);
				} else if (this.hp == 2) { // 아직 gate 안 부서짐
					System.out.println("[Gate] hp: 2");
					gateImage = new ImageIcon("image/gateHp_2.png");
					setIcon(gateImage);
				} else if (this.hp == 1) { // 아직 gate 안 부서짐
					System.out.println("[Gate] hp: 1");
					gateImage = new ImageIcon("image/gateHp_1.png");
					setIcon(gateImage);
				}
			}
		}).start();

	}

	@Override
	public void run() {
		while (!CountDown.countOver) { // countDown이 종료되면 랜덤 생성 종료
			try {

				Random rand = new Random();
				int floor = rand.nextInt(5) + 1; // 랜덤으로 생성할 층 결정
				boolean isLeft = rand.nextBoolean(); // 좌우 랜덤 생성

				// 1~5층인 경우 분기
				if (floor == 1) {
					y = 590;
					xMax = BR;
					xMin = BL;
					x = rand.nextInt(xMax - xMin) + xMin;
				} else if (floor == 2) {
					y = 400;
					if (isLeft) {
						xMax = LR;
						xMin = LL;
						x = rand.nextInt(xMax - xMin) + xMin;
					} else {
						xMax = RR;
						xMin = RL;
						x = rand.nextInt(xMax - xMin) + xMin;
					}
				} else if (floor == 3) {
					y = 290;
					xMax = MR;
					xMin = ML;
					x = rand.nextInt(xMax - xMin) + xMin;
				} else if (floor == 4) {
					y = 145;
					if (isLeft) {
						xMax = LR;
						xMin = LL;
						x = rand.nextInt(xMax - xMin) + xMin;
					} else {
						xMax = RR;
						xMin = RL;
						x = rand.nextInt(xMax - xMin) + xMin;
					}
				} else if (floor == 5) {
					y = -10;
					xMax = MR;
					xMin = ML;
					x = rand.nextInt(xMax - xMin) + xMin;
				}
				setLocation(x, y);
				Thread.sleep(7000);

				// gate 생성 시점으로부터 7초 지나면 gate 이미지 삭제
				setIcon(null);
				// 메모리 효율 위해 gate 쓰레드 멈추기
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		new Thread(this).start();
	}
}
