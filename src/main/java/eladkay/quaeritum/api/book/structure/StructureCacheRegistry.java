package eladkay.quaeritum.api.book.structure;

import java.util.HashSet;
import java.util.Set;

public class StructureCacheRegistry {

	private static Set<CachedStructure> structures = new HashSet<>();

	public static CachedStructure addStructure(String name) {
		CachedStructure structure = new CachedStructure(name, null);
		if (structure.blockInfos().isEmpty()) return null;
		structures.add(structure);

		return structure;
	}

	public static CachedStructure getStructureOrAdd(String name) {
		for (CachedStructure structure : structures) {
			if (structure.name.equals(name)) return structure;
		}

		return addStructure(name);
	}
}
