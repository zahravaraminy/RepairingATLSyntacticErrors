package utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Stream;

public class ToolBox {
	

	public static String getMutationDefinitionLine(String fileString) {
		for (String l : fileString.split("\n")) {
			if(l.startsWith("-- MUTATION"))
				return l;
		}
		throw new IllegalStateException("No mutation definition found."+fileString);
	}

	public static void copyFile(File toCopy, File targetDir, String newName, boolean fake) {
		java.nio.file.Path pSource = Paths.get(toCopy.getAbsolutePath());
		java.nio.file.Path pTarget = Paths.get(targetDir.getAbsolutePath(), newName);
		if (fake)
			System.out.println("ToolBox.copyFile[FAKE](" + pSource + ", " + pTarget + ") ");
		else
			try {
				Files.copy(pSource, pTarget, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static String extractStringFromFile(File file) throws IOException {
		String s, res = "";

		BufferedReader br3 = new BufferedReader(new FileReader(file));
		while ((s = br3.readLine()) != null) {
			s = s.trim();
			res +=  s+ "\n";
		}
		br3.close();
		s = null;
		return res;

	}

	public static String extractStringFromFile(int linesToEscape, File file) throws IOException {
		String s, res = "";

		BufferedReader br3 = new BufferedReader(new FileReader(file));
		int i = 1;
		while ((s = br3.readLine()) != null) {
			s = s.trim();
			if(i++ > linesToEscape)	
				res +=  s+ "\n";
		}
		br3.close();
		s = null;
		return res;

	}

	public static File[] getFilesWithNameEndingWith(File folder, String endsWith) {
		File[] dictFilePath = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(endsWith.toLowerCase());
			}
		});

		if (dictFilePath == null || dictFilePath.length < 1)
			throw new IllegalArgumentException(
					"Folder does not contain any '" + endsWith + "' file: '" + folder.getAbsolutePath() + "' (case incensitive)");

		return dictFilePath;
	}

	static Random random;

	public static void initializeRandom() {
		random = new Random();
	}

	public static Random getRandom() {
		return random;
	}

	public static void setRandom(Random newRandom) {
		random = newRandom;
	}

	public static double getRandomDouble() {
		return random.nextDouble();
	}

	/**
	 * Exclusive bound !
	 * 
	 * @param bound
	 *            EXCLUSIVE
	 * @return
	 */
	public static int getRandomInt(int bound) {
		return random.nextInt(bound);
	}

	/**
	 * 
	 * @param coll
	 * @return -1 if patterns is empty.
	 */
	public static int getRandomIdx(Collection<?> coll) {
		if (coll.isEmpty())
			return -1;
		return getRandomInt(coll.size());
	}

	/**
	 * 
	 * @param lower
	 *            INCLUSIVE
	 * @param upper
	 *            EXCLUSIVE
	 * @return
	 */
	public static int getRandomInt(int lower, int upper) {
		if (lower == upper)
			return lower;
		return lower + random.nextInt(upper - lower);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getRandom(Collection<T> c) {
		if (c.size() <= 0)
			return null;
		return (T) c.toArray()[getRandomInt(c.size())];
	}

	public static <T> T getRandom(T[] c) {
		if (c.length <= 0)
			return null;
		return (T) c[getRandomInt(c.length)];
	}

	public static boolean write(String csv, File f) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(csv);
		bw.close();
		return true;
	}
	static int[] countLOC(File f) throws IOException {
		int[] res = new int[] { 0, 0 };
		if (f.getName().endsWith(".java"))
			res[1]++;
		if (f.getName().startsWith("result"))
			return res;
		if (f.isDirectory()) {
			for (File f2 : f.listFiles()) {
				res[0] += countLOC(f2)[0];
				res[1] += countLOC(f2)[1];
			}
			// System.out.println("Dir:"+f.getCanonicalPath()+" : "+res);
		} else {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!line.isEmpty())
					res[0]++;
			}
			br.close();
			// System.out.println(f.getCanonicalPath()+" : "+res);
		}
		return res;
	}

	public static void printLOC() {
		int[] i;
		try {
			i = countLOC(new File("./src"));
			System.out.println("Main.main(src:" + i[0] + ") (" + i[1] + " classes)");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void copyFolder(File src, File dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src.toPath())) {
            stream.forEachOrdered(sourcePath -> {
                try {
                    Path p = Files.copy(
                            /*Source Path*/
                            sourcePath,
                            /*Destination Path */
                            dest.toPath().resolve(src.toPath().relativize(sourcePath)));
                } catch (FileAlreadyExistsException e) {
                	System.err.println("File already exsts. '" +e.getFile()+"'");
                } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            });
        }
    }}
