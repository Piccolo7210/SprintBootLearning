//package com.example.Test.Controller;
//
//import com.example.Test.Services.StudentJPQLService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/student-jpql")
//public class StudentJPQLController {
//
//    @Autowired
//    private StudentJPQLService studentJPQLService;
//
//    @GetMapping("/initialize")
//    public String initializeData() {
//        studentJPQLService.initializeSampleData();
//        return "✅ Student Management sample data initialized successfully! " +
//               "Check console for details. Created 4 departments, 6 students, and 5 courses with realistic enrollments.";
//    }
//
//    @GetMapping("/demo/basic")
//    public String demoBasicQueries() {
//        studentJPQLService.demonstrateBasicQueries();
//        return "✅ Basic JPQL queries executed successfully! " +
//               "Check console to see students with their departments and course counts. " +
//               "This demonstrates simple JOINs and basic data retrieval.";
//    }
//
//    @GetMapping("/demo/aggregation")
//    public String demoAggregationQueries() {
//        studentJPQLService.demonstrateAggregationQueries();
//        return "✅ Aggregation JPQL queries executed successfully! " +
//               "Check console for department statistics and course enrollment data. " +
//               "This demonstrates COUNT, AVG, and GROUP BY operations.";
//    }
//
//    @GetMapping("/demo/complex")
//    public String demoComplexQueries() {
//        studentJPQLService.demonstrateComplexQueries();
//        return "✅ Complex JPQL queries with subqueries executed successfully! " +
//               "Check console to see students above department average GPA and popular courses. " +
//               "This demonstrates advanced subqueries and comparative analysis.";
//    }
//
//    @GetMapping("/demo/advanced")
//    public String demoAdvancedQueries() {
//        studentJPQLService.demonstrateAdvancedQueries();
//        return "✅ Advanced JPQL queries executed successfully! " +
//               "Check console for grade categorization, capacity analysis, and performance metrics. " +
//               "This demonstrates CASE statements and complex aggregations.";
//    }
//
//    @GetMapping("/demo/joins")
//    public String demoJoinsAndConditions() {
//        studentJPQLService.demonstrateJoinsAndConditions();
//        return "✅ JPQL queries with joins and conditions executed successfully! " +
//               "Check console for filtered student data and top-performing departments. " +
//               "This demonstrates multiple JOINs with complex WHERE conditions.";
//    }
//
//    @GetMapping("/demo/all")
//    public String demoAllQueries() {
//        studentJPQLService.demonstrateBasicQueries();
//        studentJPQLService.demonstrateAggregationQueries();
//        studentJPQLService.demonstrateComplexQueries();
//        studentJPQLService.demonstrateAdvancedQueries();
//        studentJPQLService.demonstrateJoinsAndConditions();
//        studentJPQLService.showSummary();
//        return "🎉 All JPQL demonstration queries executed successfully! " +
//               "Check console for comprehensive results showing all query types: " +
//               "Basic JOINs, Aggregations, Subqueries, CASE statements, and Complex conditions. " +
//               "Perfect for learning JPQL from scratch!";
//    }
//
//    @GetMapping("/help")
//    public String showHelp() {
//        return "📚 JPQL Learning System Help:\n\n" +
//               "Available endpoints:\n" +
//               "🔧 /initialize - Create sample data (run this first!)\n" +
//               "📝 /demo/basic - Basic JPQL queries with JOINs\n" +
//               "📊 /demo/aggregation - COUNT, AVG, GROUP BY queries\n" +
//               "🎯 /demo/complex - Subqueries and advanced filtering\n" +
//               "🚀 /demo/advanced - CASE statements and complex logic\n" +
//               "🔗 /demo/joins - Multiple JOINs with conditions\n" +
//               "🎉 /demo/all - Run all demonstrations\n" +
//               "❓ /help - Show this help message\n\n" +
//               "💡 Start with /initialize, then try /demo/all to see everything!\n" +
//               "📖 Check the README.md file for detailed explanations of each query.";
//    }
//}
