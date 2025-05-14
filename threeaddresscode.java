package pr_1_2_3;

import java.util.*;
import java.util.regex.*;

public class ThreeAddressCodeGenerator {

	
	

	
	    private static int tempCount = 1;

	    static class Instruction {
	        String result;
	        String op1;
	        String operator;
	        String op2;

	        public Instruction(String result, String op1, String operator, String op2) {
	            this.result = result;
	            this.op1 = op1;
	            this.operator = operator;
	            this.op2 = op2;
	        }

	        @Override
	        public String toString() {
	            if (op2 == null) {
	                return result + " = " + op1;
	            }
	            return result + " = " + op1 + " " + operator + " " + op2;
	        }
	    }

	    private static String newTemp() {
	        return "t" + tempCount++;
	    }

	    public static List<Instruction> generateThreeAddressCode(String expression) {
	        List<Instruction> instructions = new ArrayList<>();

	        // Remove spaces
	        expression = expression.replaceAll("\\s+", "");

	        String finalResultVar = null;

	        // Extract LHS if present (like a = ...)
	        if (expression.contains("=")) {
	            int index = expression.indexOf('=');
	            finalResultVar = expression.substring(0, index);
	            expression = expression.substring(index + 1);
	        }

	        // Process parentheses first
	        while (expression.contains("(")) {
	            int start = expression.lastIndexOf('(');
	            int end = expression.indexOf(')', start);

	            if (start == -1 || end == -1) {
	                throw new IllegalArgumentException("Mismatched parentheses");
	            }

	            String subExpr = expression.substring(start + 1, end);
	            String subResult = evaluateSubExpression(subExpr, instructions);
	            expression = expression.substring(0, start) + subResult + expression.substring(end + 1);
	        }

	        // Process remaining expression
	        String resultTemp = evaluateSubExpression(expression, instructions);

	        // Final assignment to original variable
	        if (finalResultVar != null) {
	            instructions.add(new Instruction(finalResultVar, resultTemp, null, null));
	        }

	        return instructions;
	    }

	    private static String evaluateSubExpression(String expr, List<Instruction> instructions) {
	        expr = handleOperators(expr, instructions, "[*/\\^]");  // High precedence
	        expr = handleOperators(expr, instructions, "[+-]");     // Low precedence
	        return expr;
	    }

	    private static String handleOperators(String expr, List<Instruction> instructions, String ops) {
	        String regex = "(\\w+)" + "(" + ops + ")" + "(\\w+)";
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(expr);

	        while (matcher.find()) {
	            String op1 = matcher.group(1);
	            String operator = matcher.group(2);
	            String op2 = matcher.group(3);
	            String temp = newTemp();
	            instructions.add(new Instruction(temp, op1, operator, op2));
	            expr = expr.replaceFirst(Pattern.quote(matcher.group(0)), temp);
	            matcher = pattern.matcher(expr);
	        }

	        return expr;
	    }

	    public static void main(String[] args) {
	        // ✅ CHANGE YOUR EXPRESSION HERE:
	        String input = "a = m * n - o – p / q";

	        System.out.println("Input Expression: " + input);
	        System.out.println("\nGenerated Three-Address Code:\n");

	        List<Instruction> code = generateThreeAddressCode(input);
	        for (Instruction inst : code) {
	            System.out.println(inst);
	        }
	    }
	}


