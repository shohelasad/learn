Learn QA
========

How to set up an instance of learn

First of all, you need to setup an MySQL database. The default database name is 
`learn_development`, the username is `root` and the password must be blank.

To use learn

Using a compiled war file:

1. Download the war of the latest version at http://www.learn.org
2. Unpack it to a folder named `yourproject/learn`
3. Run it by executing the bash script `learn/run.sh`
4. If everything worked, you are free to customize `learn` folder as you want to! 

Using git + maven:

1. Clone the repository from [github](https://github.com/caelum/learn)
2. Install node and npm
3. Run `npm install`
5. Run `npm install -g grunt-cli`
6. Run `./scripts/mvn-package.sh`
7. Make a copy of `learn/target/learn-1.0.0-SNAPSHOT` to `yourproject/learn`
8. Run it by executing the bash script `learn/run.sh`
9. If everything worked, you are free to customize `learn` folder as you want to! 

To contribute with learn:

1. Fork the repository from [github](https://github.com/caelum/learn)
2. Clone the fork
3. Install node and npm
4. Run `npm install`
5. Run `npm install -g grunt-cli`
6. In eclipse right click on the project, select maven and to update project
7. Run `Main.java` to start learn (run from project right click, otherwise, main may not be found)
8. Develop and do your pull request

FAQ

* [How to setup an instance](http://meta.learn.org/221-how-to-set-up-an-instance-of-learn)

* [How can I boot learn in a production environment?](http://meta.learn.org/231-how-can-i-boot-learn-in-a-production-environment)

* [How can I configure ad banners in my site?](http://meta.learn.org/241-how-can-i-configure-ad-banners-in-my-site)

* [How can I configure the system to activate/deactivate some feature?](http://meta.learn.org/292-how-can-i-configure-the-system-to-activatedeactivate-some-feature)

* [How can I configure the system to allow/disallow the creation of tags by common users?](http://meta.learn.org/251-how-can-i-configure-the-system-to-allowdisallow-the-creation-of-tags-by-common-users)

* [Is it possible to delete inappropriate questions?](http://meta.learn.org/261-is-it-possible-to-delete-inappropriate-questions)

* [How can I update learn in my project?](http://meta.learn.org/271-how-can-i-update-learn-in-my-project)

* [What are the basic CSS classes to customize learn?](http://meta.learn.org/281-what-are-the-basic-css-classes-to-customize-learn)


Questions?

Send your questions to [learn meta](http://meta.learn.org).

Or to our mail-list: learn-qa-dev@googlegroups.com
