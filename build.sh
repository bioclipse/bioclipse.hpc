/usr/bin/eclipse \
    -nosplash \
    -application org.eclipse.equinox.p2.director \
    -repository http://update.bioclipse.net,http://devel.bioclipse.net,http://pele.farmbio.uu.se/jenkins/job/bioclipse.hpc/lastSuccessfulBuild/artifact/buckminster.output/net.bioclipse.hpc_site_2.5.0-eclipse.feature/site.p2/,http://download.eclipse.org/tm/updates/3.4/,http://download.eclipse.org/tools/orbit/downloads/drops/R20130827064939/repository/,http://download.eclipse.org/eclipse/updates/3.8 \
    -installIU net.bioclipse.product,net.bioclipse.statistics_feature.feature.group,net.bioclipse.hpc_feature.feature.group,org.eclipse.rse.feature.group \
    -tag Install \
    -destination ~/eclipse_build/hpc \
    -profile HPC \
    -profileProperties org.eclipse.update.install.features=true \
    -roaming

