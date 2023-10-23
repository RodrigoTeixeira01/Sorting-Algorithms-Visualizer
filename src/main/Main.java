package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import java.util.Random;

import javax.swing.JFrame;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int SCREEN_SIZE = 650;
	
	private static final int OFFSET_X = 8;
	private static final int OFFSET_Y = 31;
	
	public static final Random rand = new Random();
	
	private static final int ARRAY_SIZE = 128;
	public static int[] array = new int[ARRAY_SIZE];
	private static final int ITEM_SIZE = SCREEN_SIZE / ARRAY_SIZE;
	public static int[] marked = new int[ARRAY_SIZE];
	
	private static final int DELAY = 5000 / ARRAY_SIZE;

	private static final boolean REPEAT_FOREVER = false;

	private static final boolean MINECRAFT_MODE = false;
	private static final int TREE_ODD = 50;
	
	private static String SCRAMBLE_SHAPE = "NOISE";
	private static String SCRAMBLE_MODE = "NONE";
	private static long seed = 1L; // "deterministic" [SCRAMBLE_SHAPE]
	
	private static BufferedImage bufferImage = new BufferedImage(SCREEN_SIZE, SCREEN_SIZE, BufferedImage.TYPE_INT_RGB);
	private static Graphics g;

	protected static Main THIS;

	private Color[] color = new Color[] {
			Color.WHITE,  // default
			Color.RED,    // write or swap
			Color.BLUE,   // read
			Color.GREEN,   // check
			new Color(175, 100, 0) // dirt (MINECRAFT_MODE == true)
	};

	public Main() {
		init();
		THIS = this;
		if(MINECRAFT_MODE) {
			color[0] = Color.GRAY;
			color[1] = color[0];
			color[2] = color[0];
			color[3] = color[0];
			setBackground(Color.BLUE);
		}
		for(int i=0; i<ARRAY_SIZE; i++) {
			switch(SCRAMBLE_SHAPE) {
			case "LINEAR":
				set(i, i);
				break;
			case "SQRT":
				set(i, (int)(Math.sqrt(i)*ARRAY_SIZE/Math.sqrt(ARRAY_SIZE)));
				break;
			case "QUADRATIC":
				set(i, i*i/ARRAY_SIZE);
				break;
			case "CUBIC":
				set(i, i*i*i/ARRAY_SIZE/ARRAY_SIZE);
				break;
			case "RANDOM":
				set(i, rand.nextInt(ARRAY_SIZE));
				break;
			case "SINE":
				set(i, (int) ((Math.sin(i*3.141592653589793*2/ARRAY_SIZE)+1.0d)*ARRAY_SIZE/2));
				break;
			case "FINAL MERGE":
				set(i, (i<<1)%ARRAY_SIZE+(i>=ARRAY_SIZE>>>1?1:0));
				break;
			case "PIPE ORGAN":
				if(i < ARRAY_SIZE>>>1) {
					set(i, i<<1);
				} else {
					set(i, (ARRAY_SIZE-i<<1)-1);
				}
				break;
			case "REVERSED":
				set(i, ARRAY_SIZE-1-i);
				break;
			case "MANY SIMILAR":
				set(i, i-i%(ARRAY_SIZE>>>3));
				break;
			case "DETERMINISTIC":
				seed ^= seed << 17;
				seed ^= seed >>> 5;
				seed ^= seed << 13;
				set(i, (int) ((seed%ARRAY_SIZE+ARRAY_SIZE)%ARRAY_SIZE));
				break;
				// recommended ARRAY_SIZE to be a power of 2
			case "NOISE":
				createNoise();
				i = ARRAY_SIZE;
				break;
			case "NONE":
				throw new RuntimeException("You can't have no [SCRAMBLE_SHAPE].\n please choose another option.");
			}
		}
		do {
			switch(SCRAMBLE_MODE) {
			case "RANDOM":
				for(int i=0; i<ARRAY_SIZE-1; i++) {
					swap(i, rand.nextInt(i, ARRAY_SIZE));
				}
				break;
			case "REVERSED":
				for(int i=0; i<ARRAY_SIZE>>>1; i++) {
					swap(i, ARRAY_SIZE-1-i);
				}
				break;
			case "NONE":
				break;
			case "FEW CHANGED":
				for(int i=0; i<ARRAY_SIZE/100; i++) {
					swap(rand.nextInt(ARRAY_SIZE), rand.nextInt(ARRAY_SIZE));
				}
				break;
			case "REVERSED BIT":
				reversedBit();
				break;
			case "RANDOM FINAL MERGE":
				for(int i=0; i<ARRAY_SIZE-1; i++) {
					swap(i, rand.nextInt(i, ARRAY_SIZE));
				}
				sort(0, (array.length>>>1)-1, ((-1)>>>2)+1);
				sort(array.length>>>1, array.length-1, ((-1)>>>1)+1);
				break;
			// recommended ARRAY_SIZE to be a power of 2
			case "ALMOST SORTED":
				for(int i=0; i<ARRAY_SIZE-1; i++) {
					swap(i, rand.nextInt(i, ARRAY_SIZE));
				}
				sort(0, (array.length>>>1)-1, ((-1)>>>2)+1);
				sort(array.length>>>1, array.length-1, ((-1)>>>1)+1);
				merge();
				break;
			}
			if(Sort.ALG.equals("NONE")) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch(InterruptedException ie) {
				
			}
			Sort.sort();
			for(int i=1; i<array.length; i++) {
				if(get(i-1, false, false) > get(i)) {
					throw new RuntimeException("Not sorted on index "+(i-1)+" - "+i);
				}
				marked[i-1]=3;
			}
			for(int i=0; i<ARRAY_SIZE; i++) {
				marked[i]=0;
			}
			paint(getGraphics());
		}while(REPEAT_FOREVER);
	}
	
	private void createNoise() {
		for(int i=0; i<ARRAY_SIZE; i++) {
			set(i, i);
		}
		for(int i=0; i<ARRAY_SIZE-1; i++) {
			swap(i, rand.nextInt(i, ARRAY_SIZE));
		}
		int max = 1;
		while(max < array.length<<1) {
			max <<= 1;
		}
		int mid = max>>>2;
		sort(0, mid-1, ((-1)>>>2)+1);
		sort(mid, array.length-1, ((-1)>>>1)+1);
		merge();
		for(int i=0; i<array.length-1; i++) {
			set(i, array[i]-i+(ARRAY_SIZE>>>1));
		}
		set(array.length-1, array[array.length-2]);
		for(int i=1; i<array.length-1; i+=2) {
			set(i, (array[i-1]+array[i+1])>>>1);
		}
		set(array.length-1, array[array.length-2]);
		
		//smooth
		boolean changed;
		do {
			changed = false;
			for(int i=1; i<array.length-1; i++) {
				if((array[i]<<1)-array[i-1]-array[i+1]>1) {
					if(set(i, array[i]-1)) {
						changed = true;
					}
				}
			}
			for(int i=1; i<array.length-1; i++) {
				if((array[i]<<1)-array[i-1]-array[i+1]<-1) {
					if(set(i, array[i]+1)) {
						changed = true;
					}
				}
			}
		}while(changed);
		
		
	}

	private void merge() {
		int max = 1;
		while(max < array.length<<1) {
			max <<= 1;
		}
		merge(0, max>>>1);
	}

	private void merge(int min, int max) {
		if(max -1 <= min)
			return;
		final int mid = (min+max)>>>1;
		final int a = (min+mid)>>>1;
		final int b = (mid+max)>>>1;
		invert(a, mid);
		invert(mid, b);
		invert(a, b);
		merge(min, mid);
		merge(mid, max);
		
	}

	private void sort(int min, int max, int n) {
		if(n==0 || max <= min)
			return;
		int l = min;
		int r = max;
		while(l<r) {
			while(l<r&&(array[l]&n)==0)l++;
			while(l<r&&(array[r]&n)!=0)r--;
			swap(l, r);
		}
		if((array[l]&n)==0)
			l++;
		sort(min, l-1, n>>>1);
		sort(l, max, n>>>1);
		
	}

	private void init() {
		setSize(SCREEN_SIZE-SCREEN_SIZE%ARRAY_SIZE+OFFSET_X+8, SCREEN_SIZE-SCREEN_SIZE%ARRAY_SIZE+OFFSET_Y+8);
		setBackground(Color.BLACK);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Sorter");
		setVisible(true);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if(SCREEN_SIZE < ARRAY_SIZE) {
			throw new RuntimeException("SCREEN_SIZE must be greater or equal than ARRAY_SIZE, otherwise it would be IMPOSSIBLE to render. please change SCREEN_SIZE to a grater value or ARRAY_SIZE to a smaller one.");
		}
		new Main();
	}

	public void paint(Graphics h) {
		g = bufferImage.getGraphics();
		g.setColor(getBackground());
		g.fillRect(0, 0, SCREEN_SIZE, SCREEN_SIZE);
		
		if(MINECRAFT_MODE) {
			g.setColor(Color.YELLOW);
			g.fillRect(50, 50, 100, 100);
		}
		
		for(int i=0; i<ARRAY_SIZE; i++) {
			g.setColor(color [marked[i]]);
			//marked[i] = 0;
			g.fillRect(i*ITEM_SIZE, SCREEN_SIZE-array[i]*ITEM_SIZE-ITEM_SIZE-SCREEN_SIZE%ARRAY_SIZE, ITEM_SIZE, array[i]*ITEM_SIZE+ITEM_SIZE);
			if(MINECRAFT_MODE) {
				g.setColor(new Color(175, 100, 0));
				g.fillRect(i*ITEM_SIZE, SCREEN_SIZE-array[i]*ITEM_SIZE-SCREEN_SIZE%ARRAY_SIZE, ITEM_SIZE, ITEM_SIZE<<1);
				g.setColor(Color.GREEN);
				g.fillRect(i*ITEM_SIZE, SCREEN_SIZE-array[i]*ITEM_SIZE-ITEM_SIZE-SCREEN_SIZE%ARRAY_SIZE, ITEM_SIZE, ITEM_SIZE);
				g.setColor(Color.BLACK);
				Random temp = new Random(array[i]^i);
				if(array[i]>5)g.fillRect(i*ITEM_SIZE, SCREEN_SIZE-temp.nextInt(array[i]-5)*ITEM_SIZE-ITEM_SIZE-SCREEN_SIZE%ARRAY_SIZE, ITEM_SIZE, ITEM_SIZE);
				if(temp.nextInt(TREE_ODD)==0) {
					g.setColor(color[4]);
					g.fillRect(i*ITEM_SIZE, SCREEN_SIZE-array[i]*ITEM_SIZE-ITEM_SIZE*5-SCREEN_SIZE%ARRAY_SIZE, ITEM_SIZE, ITEM_SIZE*4);
					g.setColor(Color.GREEN);
					g.fillRect((i-2)*ITEM_SIZE, SCREEN_SIZE-array[i]*ITEM_SIZE-ITEM_SIZE*7-SCREEN_SIZE%ARRAY_SIZE, ITEM_SIZE*5, ITEM_SIZE*2);
					g.fillRect((i-1)*ITEM_SIZE, SCREEN_SIZE-array[i]*ITEM_SIZE-ITEM_SIZE*8-SCREEN_SIZE%ARRAY_SIZE, ITEM_SIZE*3, ITEM_SIZE);
				}
			}
		}
		
		
		h.drawImage(bufferImage, OFFSET_X, OFFSET_Y, null);
		setIconImage(bufferImage);
	}
	
	public static void delay() {
		try {
			Thread.sleep(DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean set(int idx, int val) {
		return set(idx, val, true);
	}
	
	public static boolean set(int idx, int val, boolean delay) {
		return set(idx, val, delay, true);
	}
	
	public static boolean set(int idx, int val, boolean delay, boolean show) {
		if(idx < 0 || idx >= ARRAY_SIZE) {
			return false;
		}
		if(array[idx] == val) {
			return false;
		}
		marked[idx] = 1;
		array[idx] = val;
		if(delay)delay();
		if(show) {
			synchronized(THIS) {
				THIS.repaint();
			}
			marked[idx] = 0;
		}
		return true;
	}
	
	public static boolean swap(int i, int j) {
		return swap(i, j, true);
	}
	
	public static boolean swap(int i, int j, boolean delay) {
		if(i < 0 || i >= ARRAY_SIZE || j < 0 || j >= ARRAY_SIZE) {
			return false;
		}
		if(array[i] == array[j]) {
			return false;
		}
		int temp = get(i, false);
		set(i, get(j, false), false, false);
		set(j, temp, delay);
		marked[i] = 0;
		return true;
	}

	public static boolean trySwap(int i, int j) {
		return trySwap(i, j, true);
	}
	
	public static boolean trySwap(int i, int j, boolean delay) {
		if(i == j || i >= array.length || j >= array.length || i < 0 || j < 0) {
			return false;
		}
		if(j < i) {
			return trySwap(j, i);
		} else if(array[i] > array[j]) {
			swap(i, j, delay);
			return true;
		}
		return false;
	}
	
	public static int get(int idx) {
		return get(idx, true);
	}
	
	public static int get(int idx, boolean delay) {
		return get(idx, delay, true);
	}

	public static int get(int idx, boolean delay, boolean draw) {
		if(idx < 0 || idx >= ARRAY_SIZE) {
			throw new InvalidParameterException("error trying to get invalid idx ("+idx+")");
		}
		marked[idx] = 2;
		if(delay)
			delay();
		if(draw) {
			synchronized(THIS) {
				THIS.repaint();
			}
			marked[idx] = 0;
		}
		return array[idx];
	}

	public static boolean tryUnSwap(int i, int j) {
		return tryUnSwap(i, j, true);
	}

	public static boolean tryUnSwap(int i, int j, boolean delay) {
		if(j < i) {
			return tryUnSwap(j, i, delay);
		}
		if(i < 0 || j >= ARRAY_SIZE) {
			return false;
		}
		if(array[i] < array[j]) {
			swap(i, j, delay);
			return true;
		}
		return false;
	}
	
	private static void reversedBit() {
		reversedBit(0, array.length-1, 1);
	}
	
	private static void reversedBit(int min, int max, int n) {
		if(max <= min || n == 0)
			return;
		int lp = min, rp = max;
		while(lp < rp) {
			while(lp < rp && (array[lp]&n)==0)
				lp++;
			while(lp < rp && (array[rp]&n)!=0)
				rp--;
			swap(lp, rp);
		}
		if((array[lp]&n)!=0)
			lp--;
		else
			rp++;
		reversedBit(min, lp, n<<1);
		reversedBit(rp, max, n<<1);
	}
	
	public static void invert(int i, int j) {
		while(i < j) {
			swap(i++, --j);
		}
	}
	
}
