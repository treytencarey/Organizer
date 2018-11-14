package model;

public class Organizer {
	private int classOrPackage;
	private String classOrPackageName;
	private int methodOrVariable;
	private String methodOrVariableName;
	private String match;
	private int order;

	public Organizer() {
	}
	
	public Organizer(int classOrPackage, String classOrPackageName, int methodOrVariable, String methodOrVariableName, String match, int order)
	{
		this.classOrPackage = classOrPackage;
		this.classOrPackageName = classOrPackageName;
		this.methodOrVariable = methodOrVariable;
		this.methodOrVariableName = methodOrVariableName;
		this.match = match;
		this.order = order;
	}

	public void setClassOrPackage(int classOrPackage)
	{
		this.classOrPackage = classOrPackage;
	}
	
	public int getClassOrPackage()
	{
		return this.classOrPackage;
	}
	
	public void setClassOrPackageName(String classOrPackageName)
	{
		this.classOrPackageName = classOrPackageName;
	}
	
	public String getClassOrPackageName()
	{
		return this.classOrPackageName;
	}
	
	public void setMethodOrVariable(int methodOrVariable)
	{
		this.methodOrVariable = methodOrVariable;
	}
	
	public int getMethodOrVariable()
	{
		return this.methodOrVariable;
	}
	
	public void setMethodOrVariableName(String methodOrVariableName)
	{
		this.methodOrVariableName = methodOrVariableName;
	}
	
	public String getMethodOrVariableName()
	{
		return this.methodOrVariableName;
	}
	
	public String getMatch()
	{
		return this.match;
	}
	
	public void setMatch(String match)
	{
		this.match = match;
	}
	
	public int getOrder()
	{
		return this.order;
	}
	
	public void setOrder(int order)
	{
		this.order = order;
	}

	@Override
	public String toString() {
		return String.valueOf(this.classOrPackage) + ":" + this.classOrPackageName + ":" + String.valueOf(this.methodOrVariable) + ":" + this.methodOrVariableName + ":" + this.match + ":" + String.valueOf(this.order);
	}
}
