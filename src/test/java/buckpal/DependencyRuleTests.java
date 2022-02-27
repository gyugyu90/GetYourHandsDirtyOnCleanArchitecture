package buckpal;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.importer.ClassFileImporter;

import buckpal.archunit.HexagonalArchitecture;

class DependencyRuleTests {

	@Test
	void validateRegistrationContextArchitecture() {
		HexagonalArchitecture.boundedContext("buckpal.account")

			.withDomainLayer("domain")

			.withAdaptersLayer("adapter")
			.incoming("in.web")
			.outgoing("out.persistence")
			.and()

			.withApplicationLayer("application")
			.services("service")
			.incomingPorts("port.in")
			.outgoingPorts("port.out")
			.and()

			.withConfiguration("configuration")
			.check(new ClassFileImporter().importPackages("buckpal.."));
	}

}
