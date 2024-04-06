package class115;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 测试链接 : https://leetcode.cn/problems/the-skyline-problem/
public class Code02_SkylineLeetcode {

	public static int MAXN = 100001;

	public static int[] sort = new int[MAXN];

	public static int[] height = new int[MAXN];

	public static int[][] heap = new int[MAXN][2];

	public static int heapSize;

	public static int build(int[][] arr, int n) {
		int size = 0;
		for (int i = 0; i < n; i++) {
			sort[size++] = arr[i][0];
			sort[size++] = arr[i][1] - 1;
			sort[size++] = arr[i][1];
		}
		Arrays.sort(sort, 0, size);
		int m = 1;
		for (int i = 1; i < size; i++) {
			if (sort[m - 1] != sort[i]) {
				sort[m++] = sort[i];
			}
		}
		for (int i = 0; i < n; i++) {
			arr[i][0] = rank(m, arr[i][0]);
			arr[i][1] = rank(m, arr[i][1] - 1);
		}
		Arrays.sort(arr, 0, n, (a, b) -> a[0] - b[0]);
		Arrays.fill(height, 0, m, 0);
		heapSize = 0;
		return m;
	}

	public static int rank(int n, int v) {
		int ans = 0;
		int l = 0, r = n - 1, mid;
		while (l <= r) {
			mid = (l + r) / 2;
			if (sort[mid] >= v) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	public static boolean isEmpty() {
		return heapSize == 0;
	}

	public static int peekHeight() {
		return heap[0][0];
	}

	public static int peekEnd() {
		return heap[0][1];
	}

	public static void push(int h, int e) {
		heap[heapSize][0] = h;
		heap[heapSize][1] = e;
		heapInsert(heapSize++);
	}

	public static void poll() {
		swap(0, --heapSize);
		heapify(0);
	}

	public static void heapInsert(int i) {
		while (compare(i, (i - 1) / 2)) {
			swap(i, (i - 1) / 2);
			i = (i - 1) / 2;
		}
	}

	public static void heapify(int i) {
		int l = i * 2 + 1;
		while (l < heapSize) {
			int best = l + 1 < heapSize && compare(l + 1, l) ? l + 1 : l;
			best = compare(best, i) ? best : i;
			if (best == i) {
				break;
			}
			swap(best, i);
			i = best;
			l = i * 2 + 1;
		}
	}

	public static boolean compare(int i, int j) {
		return heap[i][0] > heap[j][0] || (heap[i][0] == heap[j][0] && heap[i][1] < heap[j][1]);
	}

	public static void swap(int i, int j) {
		int[] tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}

	public static List<List<Integer>> getSkyline(int[][] arr) {
		int n = arr.length;
		int m = build(arr, n);
		for (int i = 0, j = 0; i < m; i++) {
			for (; j < n && arr[j][0] <= i; j++) {
				push(arr[j][2], arr[j][1]);
			}
			while (!isEmpty() && peekEnd() < i) {
				poll();
			}
			if (!isEmpty()) {
				height[i] = peekHeight();
			}
		}
		List<List<Integer>> ans = new ArrayList<>();
		for (int i = 0, pre = 0; i < m; i++) {
			if (pre != height[i]) {
				ans.add(Arrays.asList(sort[i], height[i]));
			}
			pre = height[i];
		}
		return ans;
	}

}
