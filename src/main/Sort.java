package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

public class Sort extends Main {
	private static final long serialVersionUID = 1L;
	
	public static String ALG = "NONE";
	
	public static void sort() {
		switch(ALG) {
		case "QUICK":
			quickSort();
			break;
		case "MERGE":
			mergeSort(0, array.length);
			break;
		case "INSERTION":
			insertionSort();
			break;
		case "HEAP":
			heapSort();
			break;
		case "SELECTION":
			selectionSort();
			break;
		case "BUBBLE":
			bubbleSort();
			break;
		case "STOOGE":
			stoogeSort(0, array.length-1);
			break;
		case "EXCHANGE":
			exchangeSort();
			break;
		case "MEDIANOFTHREEQUICK":
			medianOfThreeQuickSort(0, array.length-1);
			break;
		case "LLQUICK":
			LLQuickSort(0, array.length-1);
			break;
		case "RADIX":
			radixSort();
			break;
		case "BINARYQUICK":
			binaryQuickSort();
			break;
		case "MINHEAP":
			minHeapSort();
			break;
		case "CYCLE":
			cycleSort();
			break;
		case "QUICKNETWORK":
			quickNetWorkSort(0, array.length);
			break;
		case "SLOW":
			slowSort();
			break;
		case "SHELL":
			shellSort();
			break;
		case "COUNT":
			countSort();
			break;
		case "INPLACEMERGE":
			inPlaceMergeSort(0, array.length);
			break;
		case "BOZO":
			bozoSort();
			break;
		case "THREADQUICK":
			threadQuickSort(0, array.length-1);
			break;
		case "BITONIC":
			bitonicSort();
			break;
		case "OPTIMIZEDMERGE":
			optimizedMergeSort(0, array.length);
			break;
		case "OPTIMIZEDSLOW":
			optimizedSlowSort(0, array.length);
			break;
		case "WEIRD":
			weirdSort(0, array.length);
			break;
		case "NISS":
			nissSort();
			break;
		case "ANOTHERBITONIC":
			anotherBitonicSort();
			break;
		case "INTRO":
			introSort();
			break;
		case "DUALPIVOTQUICK":
			dualPivotQuickSort(0, array.length-1);
			break;
		case "TIM":
			timSort(0, array.length);
			break;
		case "PDQUICK":
			pdQuickSort();
			break;
		case "UNSTABLEINSERTION":
			unstableInsertionSort(0, array.length);
			break;
		case "PIVOTLESSQUICK":
			pivotlessQuickSort(0, array.length-1);
			break;
		case "TEST":
			testSort();
			break;
		case "NETWORK":
			networkSort();
			break;
		case "CIRCLE":
			circleSort();
			break;
		case "ODDEVEN":
			oddEvenSort();
			break;
		case "ODDEVENMERGE":
			oddEvenMergeSort();
			break;
		case "AVERAGEQUICK":
			averageQuickSort();
			break;
		default:
			throw new RuntimeException("Invalid [ALG]. Please choose a valid option (or \"NONE\" to skip the sorting step).");
		}
		THIS.repaint();
	}

	private static void averageQuickSort() {
		averageQuickSort(0, array.length-1, (int)(Math.log(array.length)/Math.log(2)));
		insertionSort(0, array.length-1);
	}

	/**
	 * Does NOT guarantee to fully sort the array
	 * @param min
	 * @param max
	 */
	private static void averageQuickSort(int min, int max, int depth) {
		if(depth <= 0) {
			return;
		}
		if(max <= min)
			return;
		if(max == min+1) {
			trySwap(min, max);
			return;
		}
		int sum = 0;
		int lowIdx = min;
		int higIdx = min;
		int lowVal = array[min];
		int higVal = lowVal;
		for(int i=min; i<=max; i++) {
			final int cur = get(i);
			sum += cur;
			if(cur < lowVal) {
				lowIdx = i;
				lowVal = cur;
			}
			if(cur > higVal) {
				higIdx = i;
				higVal = cur;
			}
		}
		trySwap(min-1, lowIdx);
		trySwap(higIdx, max+1);
		final int piv = sum / (max-min+1);
		int lp = min;
		int rp = max;
		while(lp < rp) {
			while(lp < rp && array[lp] <= piv)lp++;
			while(lp < rp && array[rp] >= piv)rp--;
			swap(lp, rp);
		}
		averageQuickSort(min, lp-1, depth-1);
		averageQuickSort(lp+1, max, depth-1);
		
	}

	private static void oddEvenMergeSort() {
		for(int p=1; p<array.length; p<<=1) {
			for(int k=p; k>=1; k>>>=1) {
				for(int j=k%p; j<=array.length-1-k; j+=2*k) {
					for(int i=0; i<=Math.min(k-1, array.length-j-k-1); i++) {
						if(((int)((i+j)/(p*2)))==((int)((i+j+k)/(p*2)))) {
							trySwap(i+j, i+j+k);
						}
					}
				}
			}
		}
		
	}

	private static void oddEvenSort() {
		for(int i=0; i < array.length; i++) {
			for(int j=(i&1)+2; j<=array.length; j+=2) {
				trySwap(j-2, j-1);
			}
		}
		
	}

	private static void testSort() {
		int max = array.length;
		max |= max >>> 1;
		max |= max >>> 2;
		max |= max >>> 4;
		max |= max >>> 8;
		max |= max >>> 16;
		max++;
		testSort(0, max>>>1);
	}

	private static void testSort(int min, int max) {
		if(max -1 <= min)
			return;
		final int mid = (min+max)>>>1;
		testSort(min, mid);
		testSort(mid, max);
		merge(min, max);
		insertionSort(min, max-1);
	}

	/**
	 * This merges two sorted parts inside the inputed range <br>
	 * this has bugs and you should use insertion sort after this
	 * @param min
	 * @param max
	 */
	private static void merge(int min, int max) {
		if(max - min < 16) {
			insertionSort(min, max-1);
			return;
		}
		int mid = min;
		while(array[mid] < array[mid+1] && mid < max-2) {
			mid++;
		}
		if(mid == max-2)
			return;
		mid++;
		final int quarter = (min+mid)>>>1;
		int i = mid;
		while(array[i] < array[quarter] && i < max-1)
			i++;
		if(i == max-1)
			return;
		invert(quarter, mid);
		invert(mid, i);
		invert(quarter, i);
		mid = (quarter+i+1)>>>1;
		merge(min, mid+1);
		merge(mid, max);
		
	}

	private static void circleSort() {
		for(int n = array.length; n > 0; n>>>=1) {
			if(circleSort(0, array.length-1))
				break;
		}
	}
	
	private static boolean circleSort(int min, int max) {
		if(max <= min)
			return true;
		boolean ret = true;
		int i = min, j = max;
		while(i < j)
			if(trySwap(i++, j--))
				ret = false;
		ret &= circleSort(min, i-1);
		ret &= circleSort(i, max);
		return ret;
		
	}

	private static void networkSort() {
		File file = new File("network.network");
		try (InputStream in = new FileInputStream(file)) {
			byte[] bytes = in.readAllBytes();
			for(int i=0; i<bytes.length; i+=2) {
				trySwap(bytes[i], bytes[i+1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private static void pivotlessQuickSort(int min, int max) {
		if(max <= min) {
			return;
		}
		int lp = min, rp = max;
		while(lp < rp) {
			while(lp < rp && array[lp] <= array[rp]) {
				lp++;
			}
			swap(lp, rp);
			while(lp < rp && array[lp] <= array[rp]) {
				rp--;
			}
			swap(lp, rp);
		}
		pivotlessQuickSort(min, lp-1);
		pivotlessQuickSort(lp, max);
		
	}

	/**
	 * first inclusive, last exclusive
	 * @param min
	 * @param max
	 */
	private static void unstableInsertionSort(int min, int max) {
		int size = min;
		for(int i=size+1; i<max; i++) {
			if(array[i] > array[size]) {
				swap(i, ++size);
			}
		}
		swap(size, max-1);
		for (int i = size; i < max; i++) {
	        int low = min;
	        int hig = i - 1;
	        int mid = (low + hig) >>> 1;

	        while (low <= hig) {
	            mid = (low + hig) >>> 1;

	            if (array[mid] > array[i]) {
	                hig = mid - 1;
	            } else {
	                low = mid + 1;
	            }
	        }

	        for (int j = i; j > low; j--) {
	            swap( j, j - 1);
	        }
	    }
		
	}

	private static void pdQuickSort() {
		pdQuickSort(0, array.length-1, (int) (2.885390082*Math.log(array.length)), false);
	}
	
	private static void pdQuickSort(int min, int max, int depth, boolean runInsertion) {
		if(max <= min) {
			return;
		}
		if(max -16 <= min) {
			//insertionSort(min, max);
			unstableInsertionSort(min, max+1);
			return;
		}
		if(runInsertion) {
			boolean dir = array[max] > array[min]; // true = ascending; false = descending
			int c = 0;
			for(int i=min; i<=max; i++) {
				int j = i;
				while(--j > min) {
					if((array[j+1] > array[j]) ^ dir){
						swap(j, j+1);
						c++;
						if(c >= 8) {
							i = max;
							break;
						}
					} else {
						break;
					}
				}
			}
			if(!dir) {
				int i=min;
				int j=max;
				while(i < j) {
					swap(i++, j--);
				}
			}
			if(c < 8) {
				return;
			}
		}
		if(depth==0) {
			heapSort(min, max);
			return;
		}
		boolean insert = true;
		final int piv = array[max];
		int lp = min, rp = max;
		while(lp < rp) {
			while(lp < rp && array[lp] <= piv)lp++;
			while(lp < rp && array[rp] >= piv)rp--;
			if(swap(lp, rp))insert = false;
		}
		if(swap(lp, max))insert=false;
		pdQuickSort(min, lp-1, depth-1, insert);
		pdQuickSort(lp+1, max, depth-1, insert);
		
		
	}

	private static void timSort(int min, int max) {
		if(max -1 <= min)
			return;
		boolean dir = array[min+1] > array[min];
		if(max - 16 <= min) {
			insertionSort(min, max-1, dir);
			return;
		}
		boolean isSorted = true;
		int last = get(min);
		for(int i=min+1; i<max; i++) {
			int cur = get(i);
			if((cur > last)^dir) {
				isSorted = false;
				break;
			}
			last = cur;
		}
		if(isSorted) {
			if(!dir) {
				int i = min;
				int j = max;
				while(i < j) {
					swap(i++, --j);
				}
			}
			return;
		}
		final int mid = (min+max)>>>1;
		timSort(min, mid);
		timSort(mid, max);
		int[] copy = new int[mid-min];
		for(int i=min; i<mid; i++) {
			copy[i-min] = get(i);
		}
		int i = 0; // copy array index (read)
		int j = min; // real array index (set)
		int k = mid; // read array index (second half)
		while(i < copy.length && k < max) {
			int temp = get(k);
			if(copy[i] < temp) {
				set(j++, copy[i++]);
			} else {
				set(j++, temp);
				k++;
			}
		}
		while(i < copy.length) {
			set(j++, copy[i++]);
		}
	}

	/**
	 * last inclusive
	 * true DIR means ascending<br>
	 * false DIR means descending
	 * @param min
	 * @param max
	 * @param dir
	 */
	private static void insertionSort(int min, int max, boolean dir) {
		for(int i=min+1; i<=max; i++) {
			int j=i;
			while(j > min) {
				if((array[j] > array[--j]) ^ dir) {
					swap(j, j+1);
				} else {
					break;
				}
			}
		}
		if(!dir) {
			int i=min;
			int j=max;
			while(i < j) {
				swap(i++, j--);
			}
		}
		
	}

	private static void dualPivotQuickSort(int min, int max) {
		if(max <= min)
			return;
		
		trySwap(min, max);
		final int piv1 = array[min], piv2= array[max];
		int a = min+1, b = min+1, c = max-1;
		while(b <= c) {
			if (array[b] < piv1) {
				swap(a++, b++);
			} else if (array[b] > piv2) {
				swap(b, c--);
			} else {
				b++;
			}
		}
		swap(min, a-1);
		swap(max, b);
		
		dualPivotQuickSort(min, a-2);
		dualPivotQuickSort(a, b-1);
		dualPivotQuickSort(b+1, max);
		
		/*final int _a = a;
		final int _b = b;
		Thread t1 = new Thread(()->dualPivotQuickSort(min, _a-2));
		Thread t2 = new Thread(()->dualPivotQuickSort(_a, _b-1));
		Thread t3 = new Thread(()->dualPivotQuickSort(_b+1, max));
		t1.start();
		t2.start();
		t3.start();
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}

	private static void introSort() {
		introSort(0, array.length-1, (int) (2.885390082*Math.log(array.length)));
	}

	private static void introSort(int min, int max, int depth) {
		if(max <= min)
			return;
		if(max - min <= 16) {
			insertionSort(min, max);
			return;
		}
		if(depth <= 0) {
			heapSort(min, max);
			return;
		}
		final int piv = array[max];
		int lp = min, rp = max;
		while(lp < rp) {
			while(lp < rp && array[lp] <= piv)lp++;
			while(lp < rp && array[rp] >= piv)rp--;
			swap(lp, rp);
		}
		swap(lp, max);
		introSort(min, lp-1, depth-1);
		introSort(lp+1, max, depth-1);
		
	}
	
	/**
	 * made by Chat-GPT<br><br>
	 * heap sorts every element from low to high<br>
	 * exclusive with last item
	 * @param low
	 * @param high
	 */
	private static void heapSort(int low, int high) {
        int n = high - low + 1;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(n, i, low);
        }
        for (int i = n - 1; i > 0; i--) {
            swap(low, low+i);
            heapify(i, 0, low);
        }
    }

	/**
	 * also made by Chat-GPT<br><br>
	 * heap sort helper function
	 * @param n
	 * @param i
	 * @param offset
	 */
    private static void heapify(int n, int i, int offset) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && array[offset + left] > array[offset + largest]) {
            largest = left;
        }
        if (right < n && array[offset + right] > array[offset + largest]) {
            largest = right;
        }
        if (largest != i) {
            swap(offset+i, offset+largest);
            heapify(n, largest, offset);
        }
    }

	private static void anotherBitonicSort() {
		int max = array.length;
		max |= max >>> 1;
		max |= max >>> 2;
		max |= max >>> 4;
		max |= max >>> 8;
		max |= max >>> 16;
		anotherBitonicSort(0, max+1);
		
	}

	private static void anotherBitonicSort(int min, int max) {
		if(max -1 <= min)
			return;
		final int mid = (min+max)>>>1;
		anotherBitonicSort(min, mid);
		anotherBitonicSort(mid, max);
		int i = min;
		int j = max;
		while(i < j) {
			trySwap(i++, --j);
		}
		anotherBitonicSort(min, mid);
		anotherBitonicSort(mid, max);
	}

	/**
	 * GLITCHED WITH RANDOM ARRAYS
	 */
	private static void nissSort() {
		int[] copy = Arrays.copyOf(array, array.length);
		
		LinkedList<Integer> swaps = nissSort(copy, new LinkedList<>(), 0, copy.length-1);
		Object[] swapArr = swaps.toArray();
		for(int i=0; i<swapArr.length; i+=2) {
			int temp = copy[(int)swapArr[i]];
			copy[(int)swapArr[i]] = copy[(int)swapArr[i+1]];
			copy[(int)swapArr[i+1]] = temp;
		}
		
		swaps.clear();
		swaps = nissSort(copy, swaps, 0, copy.length-1);
		swapArr = swaps.toArray();
		for(int i=swapArr.length-2; i >= 0; i-=2) {
			swap((int)swapArr[i], (int)swapArr[i+1]);
		}
	}

	private static LinkedList<Integer> nissSort(int[] array, LinkedList<Integer> swaps, int min, int max) {
		if(max <= min) {
			return swaps;
		}
		final int piv = array[max];
		int lp = min;
		int rp = max;
		while(lp < rp) {
			while(lp < rp && array[lp] <= piv)lp++;
			while(lp < rp && array[rp] >= piv)rp--;
			int temp = array[lp];
			array[lp] = array[rp];
			array[rp] = temp;
			swaps.add(lp);
			swaps.add(rp);
		}
		
		int temp = array[lp];
		array[lp] = array[max];
		array[max] = temp;
		swaps.add(lp);
		swaps.add(max);
		
		nissSort(array, swaps, min, lp-1);
		nissSort(array, swaps, lp+1, max);
		
		return swaps;
	}

	/**
	 * Yes I just copied the "optimized-merge" script from myself
	 * @param min
	 * @param max
	 */
	private static void weirdSort(int min, int max) {
		if(max -1 <= min)
			return;
		THIS.setTitle("Why??");
		final int mid = (min+max)>>>1;
		Thread t1 = new Thread(()->weirdQuickSort(min, mid-1));
		Thread t2 = new Thread(()->weirdQuickSort(mid, max-1));
		t1.start();
		t2.start();
		try {
			t1.join();
		} catch(InterruptedException ie) {
			ie.printStackTrace();
		}
		int[] copy = new int[mid-min];
		for(int i=min; i<mid; i++) {
			copy[i-min] = get(i);
		}
		try {
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int i = 0; // copy array index (read)
		int j = min; // real array index (set)
		int k = mid; // read array index (second half)
		while(i < copy.length && k < max) {
			int temp = get(k);
			if(copy[i] < temp) {
				set(j++, copy[i++]);
			} else {
				set(j++, temp);
				k++;
			}
		}
		while(i < copy.length) {
			set(j++, copy[i++]);
		}
	}

	private static void weirdQuickSort(int min, int max) {
		if(max <= min)
			return;
		final int mid = (min+max)>>>1;
		trySwap(mid, max);
		trySwap(min, mid);
		if(array[mid] < array[max]) {
			swap(min, max);
		}
		final int piv = array[max];
		int lp = min, rp = max;
		while (lp < rp) {
			while (array[lp] <= piv && lp < rp)
				lp++;
			while (array[rp] >= piv && lp < rp)
				rp--;
			swap(lp, rp);
		}
		swap(lp, max);
		final int temp = lp;
		Thread t1 = new Thread(()->weirdSort(min, temp));
		Thread t2 = new Thread(()->weirdSort(temp+1, max+1));
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		}catch(InterruptedException ie) {
			ie.printStackTrace();
		}
		
	}

	private static void optimizedSlowSort(int min, int max) {
		if(max-1 <= min) {
			return;
		}
		final int mid = (min+max)>>>1;
		optimizedSlowSort(min, mid);
		optimizedSlowSort(mid, max);
		trySwap(min, mid);
		trySwap(mid-1, max-1);
		optimizedSlowSort(min+1, max-1);
		
	}

	private static void optimizedMergeSort(int min, int max) {
		if(max-1 <= min)return;
		int mid = (min+max)>>>1;
		optimizedMergeSort(min, mid);
		optimizedMergeSort(mid, max);
		int[] copy = new int[mid-min];
		for(int i=min; i<mid; i++) {
			copy[i-min] = get(i);
		}
		int i = 0; // copy array index (read)
		int j = min; // real array index (set)
		int k = mid; // read array index (second half)
		while(i < copy.length && k < max) {
			int temp = get(k);
			if(copy[i] < temp) {
				set(j++, copy[i++]);
			} else {
				set(j++, temp);
				k++;
			}
		}
		while(i < copy.length) {
			set(j++, copy[i++]);
		}
		
	}

	private static void bitonicSort() {
		int max = array.length;
		max |= max >>> 1;
		max |= max >>> 2;
		max |= max >>> 4;
		max |= max >>> 8;
		max |= max >>> 16;
		max++;
		File file = new File("network.network");
		try {
			OutputStream out = new FileOutputStream(file);
			bitonicSort(0, max, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void bitonicSort(int min, int max, OutputStream out) {
		if(max-1 <= min)
			return;
		if(max -2 == min) {
			try {
				out.write(new byte[] {(byte) min, (byte) (max-1)});
			} catch (IOException e) {
				e.printStackTrace();
			}
			trySwap(min, max-1);
			return;
		}
		if(max -3 == min) {
			try {
				out.write(new byte[] {(byte) min, (byte)(min+1), (byte)(min+1), (byte) (max-1), (byte) min, (byte)(min+1)});
			} catch (IOException e) {
				e.printStackTrace();
			}
			trySwap(min, min+1);
			trySwap(min+1, max-1);
			trySwap(min, min+1);
			return;
		}
		if(max -4 == min) {
			try {
				out.write(new byte[] {
						(byte)(min),
						(byte)(min+1),
						(byte)(min+2),
						(byte)(min+3),
						(byte)(min),
						(byte)(min+2),
						(byte)(min+1),
						(byte)(min+3),
						(byte)(min+1),
						(byte)(min+2)
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			trySwap(min, min+1);
			trySwap(min+2, min+3);
			trySwap(min, min+2);
			trySwap(min+1, min+3);
			trySwap(min+1, min+2);
			return;
		}
		final int mid = (max+min+1)>>>1;
		bitonicSort(min, mid, out);
		bitonicSort(mid, max, out);
		int lp = min, rp = max;
		while(lp < rp) {
			trySwap(lp++, --rp);
			try {
				out.write(new byte[] {(byte) (lp-1), (byte) rp});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		bitonicMerge(min, mid, out);
		bitonicMerge(mid, max, out);
		
	}

	private static void bitonicMerge(int min, int max, OutputStream out) {
		if(max-1 <= min)
			return;
		final int mid = (min+max+1)>>>1;
		int lp = min, rp = mid;
		while(lp < mid && rp < max) {
			try {
				out.write(new byte[] {(byte) lp, (byte) rp});
			} catch (IOException e) {
				e.printStackTrace();
			}
			trySwap(lp++, rp++);
		}
		bitonicMerge(min, mid, out);
		bitonicMerge(mid, max, out);
	}

	private static void threadQuickSort(int min, int max) {
		if(max <= min) {
			return;
		}
		int lp = min, rp = max;
		final int piv = array[max];
		while(lp < rp) {
			while(lp < rp && array[lp] <= piv)lp++;
			while(lp < rp && array[rp] >= piv)rp--;
			swap(lp, rp);
		}
		swap(lp, max);
		final int temp = lp;
		Thread t1 = new Thread(()->threadQuickSort(min, temp-1));
		Thread t2 = new Thread(()->threadQuickSort(temp+1, max));
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

	private static void bozoSort() {
		while(!sorted()) {
			trySwap(rand.nextInt(0, array.length), rand.nextInt(0, array.length));
		}
		
	}

	private static boolean sorted() {
		for(int i=1; i<array.length; i++) {
			if(array[i-1]>array[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * May be unstable and change over the versions
	 * @param min
	 * @param max
	 */
	private static void inPlaceMergeSort(int min, int max) {
		if(max-1 <= min) {
			return;
		}
		if(max-16 <= min) {
			unstableInsertionSort(min, max);
			return;
		}
		int mid = (min+max)>>>1;
		inPlaceMergeSort(min, mid);
		inPlaceMergeSort(mid, max);
		unstableMerge(min, max);
		insertionSort(min, max-1);
	}

	private static void unstableMerge(int min, int max) {
		if(max -1 <= min)
			return;
		final int mid = (min+max)>>>1;
		final int a = (min+mid)>>>1;
		final int b = (mid+max)>>>1;
		invert(a, mid);
		invert(mid, b);
		invert(a, b);
		unstableMerge(min, mid);
		unstableMerge(mid, max);
		
	}

	@SuppressWarnings("unused")
	private static void bitonicMerge(int min, int max) {
		if(max -1 <= min)
			return;
		int mid = (min+max+1)>>>1;
		int lp = (min+mid+1)>>>1;
		int rp = mid;
		while(lp < mid) {
			trySwap(lp++, rp++);
		}
		bitonicMerge(min, mid);
		bitonicMerge(mid, max);
		
		
	}

	private static void countSort() {
		int min = get(0);
		int max = min;
		
		for(int i=1; i<array.length; i++) {
			int temp = get(i);
			if (temp < min) {
				min = temp;
			}
			if (temp > max) {
				max = temp;
			}
		}
		
		int[] count = new int[max-min+1];
		for(int i=0; i<array.length; i++) {
			count[get(i)-min]++;
		}
		
		int size = 0;
		for(int i=0; i<count.length; i++) {
			for(int j=0; j<count[i]; j++) {
				set(size++, i+min);
			}
		}
		
	}

	private static void shellSort() {
		boolean changed = true;
		int gap = array.length;
		while (gap >= 1 && (gap!=1 || changed)) {
			gap /= 2.3d;
			changed = false;
			if(gap == 0)gap = 1;
			for(int i=0; i<array.length-gap; i++) {
				int j = i;
				while(j >= 0 ? trySwap(j, j+gap) : false) {
					changed = true;
					j -= gap;
				}
			}
		}
	}

	private static void slowSort() {
		slowSort(0, array.length-1);
	}

	private static void slowSort(int min, int max) {
		if(max <= min) {
			return;
		}
		int mid = (min+max)>>>1;
		slowSort(min, mid);
		slowSort(mid+1, max);
		trySwap(mid, max);
		slowSort(min, max-1);
	}
	
	/**
	 * not so quick...
	 */
	private static void quickNetWorkSort(int min, int max) {
		if (max-1 <= min)
			return;
		final int mid = (min+max) >>> 1;
		for(int i=min; i<mid; i++) {
			for(int j=max-1; j>=mid; j--) {
				trySwap(i, j);
			}
		}
		quickNetWorkSort(min, mid);
		quickNetWorkSort(mid, max);
	}

	private static void cycleSort() {
		boolean[] sorted = new boolean[array.length];
		for(int i=0; i<array.length; i++) {
			int pos;
			if (!sorted[i]) do {
				pos = i;
				int cur = get(i);
				for(int j = i+1; j<array.length; j++) {
					if(get(j) < cur) {
						pos++;
					}
				}
				swap(i, pos);
				sorted[pos] = true;
			}while(pos > i);
		}
		
	}

	private static void minHeapSort() {
		for(int i=1; i<array.length; i++) {
			int next = 1;
			int cur = i;
			while(next > 0) {
				next = (cur-1)>>>1;
				if(!trySwap(next, cur))break;
				cur = next;
			}
		}
		int next;
		for(int i=array.length-1; i>0; i--) {
			swap(0, i);
			for(int j=0; true; j=next) {
				next = (j<<1)+1;
				if(next >= i)break;
				if(next+1<i&&array[next]>array[next+1])next++;
				if(array[j] <= array[next])break;
				swap(j, next);
			}
		}
		int i=0, j=array.length;
		while(i < --j)
			swap(i++, j);
		
	}

	private static void binaryQuickSort() {
		int m = 1;
		int size = array.length;
		while(size > 0) {
			size >>= 1;
			m <<= 1;
		}
		binaryQuickSort(0, array.length-1, m>>1);
	}

	private static void binaryQuickSort(int min, int max, int n) {
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
		binaryQuickSort(min, lp, n>>>1);
		binaryQuickSort(rp, max, n>>>1);
	}

	private static void radixSort() {
		int m = get(0);
		for(int i=1; i<array.length; i++) {
			if(get(i) > m) {
				m = array[i];
			}
		}
		for(int exp=1; m>exp; exp<<=1) {
			int output[] = new int[array.length];
			int count[] = new int[2];
			int i;
			for(i=0; i<array.length; i++)
				count[(get(i) & exp)>0?1:0]++;
			count[1]+=count[0];
			for(i=array.length-1; i>=0; i--) {
				output[--count[(array[i]&exp)>0?1:0]] = array[i];
			}
			for(i=0; i<array.length; i++) {
				set(i, output[i]);
			}
		}
	}
	
	private static void LLQuickSort(int min, int max) {
		if(max <= min)
			return;
		/*int mid = (min+max)>>>1;
		trySwap(mid, max);
		trySwap(min, mid);
		if(array[mid] < array[max]) {
			swap(min, max);
		}*/
		int piv = array[max];
		int i = min;
		for(int j=min; j<max; j++) {
			if(array[j] <= piv) {
				swap(i++, j);
			}
		}
		swap(i, max);
		LLQuickSort(min, i-1);
		LLQuickSort(i+1, max);
	}
	private static void mergeSort(int min, int max) {
		if(max-1 <= min) {
			return;
		}
		int mid = (min+max)>>>1;
		mergeSort(min, mid);
		mergeSort(mid, max);
		int[] sorted = new int[max-min];
		int lp = min, rp = mid;
		int size = 0;
		while(lp < mid && rp < max) {
			if(array[lp] <= array[rp]) {
				sorted[size++] = get(lp++);
			}else {
				sorted[size++] = get(rp++);
			}
		}
		while(lp < mid) {
			sorted[size++] = get(lp++);
		}
		while(rp < max) {
			sorted[size++] = get(rp++);
		}
		for(int i=0; i<size; i++) {
			set(i+min, sorted[i]);
		}
		
	}
	private static void medianOfThreeQuickSort(int min, int max) {
		if(max <= min)return;
		if(max == min+1) {
			trySwap(min, max);
			return;
		}
		int lp = min, rp = max;
		final int mid = (min+max)>>>1;
		trySwap(mid, max);
		trySwap(min, mid);
		if(array[mid] < array[max]) {
			swap(mid, max);
		}
		final int piv = array[max];
		while(lp < rp) {
			while(lp < rp && array[lp] <= piv) {
				lp++;
			}
			while(lp < rp && array[rp] >= piv) {
				rp--;
			}
			swap(lp, rp);
		}
		swap(lp, max);
		medianOfThreeQuickSort(min, lp-1);
		medianOfThreeQuickSort(lp+1, max);
		
	}
	private static void exchangeSort() {
		for(int i=0; i<array.length; i++) {
			for(int j=array.length-1; j>i; j--) {
				trySwap(i, j);
			}
		}
		
	}
	private static void stoogeSort(int min, int max) {
		if(max - 1 <= min) {
			trySwap(min, max);
			return;
		}
		int diff = max - min;
		int temp = (int) Math.ceil(((double)diff)*2.0d/3.0d) + min;
		if(temp >= max)temp = max-1;
		stoogeSort(min, temp);
		stoogeSort((int)(min+Math.ceil(((double)diff)/3.0d)), max);
		stoogeSort(min, temp);
	}
	
	private static void bubbleSort() {
		for(int i=array.length; i>0; i--) {
			boolean changed = false;
			for(int j=1; j<i; j++) {
				if(trySwap(j-1, j))
					changed=true;
			}
			//delay();
			if(!changed)
				return;
		}
		
	}

	private static void selectionSort() {
		for(int i=0; i<array.length; i++) {
			int bestIdx = i;
			int best = get(i);
			for(int j=i+1; j<array.length; j++) {
				int current = get(j);
				if(current < best) {
					bestIdx = j;
					best = current;
				}
			}
			swap(i, bestIdx);
		}
		
	}

	private static void heapSort() {
		for(int i=0; i<array.length; i++) {
			int j = i;
			int next = i;
			while(next > 0) {
				next = (j-1)>>>1;
				if(array[j]>array[next]) {
					swap(j, next);
				}else {
					break;
				}
				j=next;
			}
		}
		int next;
		for(int i=array.length-1; i>0; i--) {
			swap(0, i);
			for(int j=0; true; j=next) {
				next = (j<<1)+1;
				if(next >= i)break;
				if(next+1<i&&array[next]<array[next+1])next++;
				if(array[j] >= array[next])break;
				swap(j, next);
			}
		}
	}

	private static void insertionSort() {
		insertionSort(0, array.length-1);
		
	}

	private static void insertionSort(int min, int max) {
		for(int i=min+1; i<=max; i++) {
			for(int j=i; trySwap(j-1, j) && --j > min;);
		}
		
	}

	private static void quickSort() {
		quickSort(0, array.length-1);
	}

	private static void quickSort(int min, int max) {
		if(min >= max)return;
		int lp = min, rp = max, piv = array[max];
		while(lp<rp) {
			while(lp<rp && array[lp] <= piv)lp++;
			while(lp<rp && array[rp] >= piv)rp--;
			swap(lp, rp);
		}
		swap(lp, max);
		quickSort(min, lp-1);
		quickSort(lp+1, max);
	}

}
