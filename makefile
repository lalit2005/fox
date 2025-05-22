compile:
	javac sh/lalit/fox/Fox.java

run:
	rm -rf sh/lalit/fox/*.class && javac sh/lalit/fox/Fox.java && java sh.lalit.fox.Fox test.fox

clean:
	rm -rf sh/lalit/fox/*.class

gen-ast:
	javac sh/lalit/tools/GenerateAst.java && java sh.lalit.tools.GenerateAst sh/lalit/fox
