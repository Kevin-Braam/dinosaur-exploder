package com.code.analysis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class App {

    public static void main(String[] args) {
        // Build the AST of the dinosaur repo
        Launcher launcher = new Launcher();
        launcher.addInputResource("../dinosaur/src/main/java");
        launcher.getEnvironment().setNoClasspath(true);

        CtModel ast = launcher.buildModel();
        // Only include the top level packages of dinosaur repository
        Collection<CtPackage> packages = ast.getAllPackages().stream().filter(p -> p.getQualifiedName().startsWith("com.dinosaur.dinosaurexploder.") && p.getQualifiedName().split("[.]").length <= 4).toList();

        for (CtPackage pck : packages) {
            double intraconnectivity = computeIntraconnectivity(pck);
            System.out.println(String.format("Package %s has intra-connectivity %.2f", pck.getSimpleName(), intraconnectivity));
        }    
    }

    private static double computeIntraconnectivity(CtPackage pck) {
        List<CtType> types = pck.getElements(new TypeFilter<>(CtType.class)).stream().filter(t -> !t.isAnonymous()).toList();
        // System.out.println("Package %s: " + pck.getSimpleName());
        // types.forEach(t -> System.out.println("  " + t.getQualifiedName()));

        int edgeCount = 0;
        for (CtType<?> type : types) {
            Set<CtTypeReference<?>> internalReferences = type.getReferencedTypes().stream()
            .filter(r -> !r.isPrimitive()) // No primitive types
            .filter(r -> r.getTypeDeclaration() != null && r.getTypeDeclaration().getPackage() != null) // Null pointer prevention
            .filter(r -> r.getTypeDeclaration().getPackage().getQualifiedName().startsWith(pck.getQualifiedName())) // Internal reference check
            .filter(r -> types.stream().anyMatch(t -> r.getDeclaration().equals(t)))
            .collect(Collectors.toSet());

            // Uncomment to see what is counted
            // System.out.println(String.format("Type %s references: ", type.getQualifiedName()));
            // internalReferences.forEach(r -> System.out.println(String.format("    %s", r.getQualifiedName())));
            
            edgeCount += internalReferences.size();
            edgeCount -= 1; // Don't count the self loop
        }

        int nodeCount = types.size();
        // System.out.println(String.format("Edge: %d; Node: %d;", edgeCount, nodeCount));
        return ((double) edgeCount) / (nodeCount * nodeCount);
    }
}
