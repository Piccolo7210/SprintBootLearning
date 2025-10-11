//package com.example.Test.Services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JPQLConsoleDemo implements CommandLineRunner {
//
//    @Autowired
//    private StudentJPQLService studentJPQLService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // This will run automatically when the application starts
//        System.out.println("\n" + "=".repeat(60));
//        System.out.println("🎓 JPQL LEARNING SYSTEM - CONSOLE DEMO");
//        System.out.println("=".repeat(60));
//
//        System.out.println("📚 Welcome to the JPQL Learning System!");
//        System.out.println("💡 This system demonstrates complex JPQL queries with:");
//        System.out.println("   • Student-Department-Course relationships");
//        System.out.println("   • JOINs, Aggregations, and Subqueries");
//        System.out.println("   • Real-world university data scenarios");
//
//        System.out.println("\n🚀 To get started, visit these endpoints:");
//        System.out.println("   GET http://localhost:8080/api/student-jpql/initialize");
//        System.out.println("   GET http://localhost:8080/api/student-jpql/demo/all");
//        System.out.println("   GET http://localhost:8080/api/student-jpql/help");
//
//        System.out.println("\n📖 Check README.md for detailed learning guide!");
//        System.out.println("=".repeat(60) + "\n");
//
//        // Uncomment the lines below if you want automatic demo on startup
//        // System.out.println("🔧 Auto-initializing sample data...");
//        // studentJPQLService.initializeSampleData();
//        // System.out.println("\n🎉 Running all JPQL demonstrations...");
//        // studentJPQLService.demonstrateBasicQueries();
//        // studentJPQLService.demonstrateAggregationQueries();
//        // studentJPQLService.demonstrateComplexQueries();
//        // studentJPQLService.demonstrateAdvancedQueries();
//        // studentJPQLService.demonstrateJoinsAndConditions();
//        // studentJPQLService.showSummary();
//    }
//}
