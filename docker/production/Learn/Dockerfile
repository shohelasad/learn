FROM java:8-jdk
MAINTAINER Roy Meissner, https://rmeissn.github.io

# learn VERSION --> Tested for v1.3 and 1.4
# On change rember to edit the variables also in ../docker-compose.yml
ENV VERSION=1.4.0 USER=learn PASSWORD=Shei6cea DB=db/learn_db

RUN mkdir /learn
WORKDIR /learn

RUN wget -nv https://github.com/caelum/learn/releases/download/v${VERSION}/learn-${VERSION}.war && \
    unzip learn-${VERSION}.war && \
    rm learn-${VERSION}.war

ADD ./production.properties /learn/WEB-INF/classes/production.properties

RUN if [ "$VERSION" = "1.3.0" ]; then mv /learn/WEB-INF/classes/production/hibernate.cfg.xml.example /learn/WEB-INF/classes/production/hibernate.cfg.xml; fi

RUN sed -i -e "s?>root<?>$USER<?g" /learn/WEB-INF/classes/production/hibernate.cfg.xml && \
    sed -i -e "s?><?>$PASSWORD<?g" WEB-INF/classes/production/hibernate.cfg.xml && \
    sed -i -e "s?localhost/learn_production?$DB?g" WEB-INF/classes/production/hibernate.cfg.xml

RUN chmod +x run.sh

EXPOSE 8080

# Wait for intial Database init --> TODO: Find a better way to wait for the DB
CMD sleep 15 && VRAPTOR_ENV=production ./run.sh
